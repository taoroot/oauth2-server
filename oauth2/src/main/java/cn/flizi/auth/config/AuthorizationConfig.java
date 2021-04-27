package cn.flizi.auth.config;

import cn.flizi.auth.security.*;
import cn.flizi.auth.security.social.SocialCodeTokenGranter;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.*;

/**
 * Security Security OAuth 配置
 */
@Configuration
@EnableAuthorizationServer
@AllArgsConstructor
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final DataSource dataSource;

    private final AuthorizationServerProperties authorizationServerProperties;

    private final CaptchaService captchaService;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.authenticationEntryPoint(restAuthenticationEntryPoint);
        security.allowFormAuthenticationForClients();
    }

    /**
     * 配置 Spring MVC Controller
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // @formatter:off
        endpoints
                .exceptionTranslator(new CustomWebResponseExceptionTranslator())
                .tokenEnhancer(tokenEnhancer())
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailsService)         // refresh_token 模式,通过用户名,放回用户信息
                .authenticationManager(authenticationManager)   // password 模式, 验证密码, 返回用户登录信息
                .tokenGranter(tokenGranter(endpoints, authenticationManager));
        // @formatter:on
    }

    /**
     * 配置客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    /**
     * 扩展支持 social 模式, 并且 password 模式添加验证码
     */
    public TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints,
                                     AuthenticationManager authenticationManager) {
        TokenGranter tokenGranter = endpoints.getTokenGranter();
        return (grantType, tokenRequest) -> {
            // 密码模式添加验证码匹配
            if (grantType.equalsIgnoreCase("password")) {
                HttpServletRequest request = ((ServletRequestAttributes) Objects
                        .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
                String key = request.getParameter("captchaKey");
                String value = request.getParameter("captchaCode");
                if (!captchaService.check(key, value)) {
                    throw new InvalidGrantException("验证码错误");
                }
            }
            OAuth2AccessToken grant = tokenGranter.grant(grantType, tokenRequest);
            if (grant != null) {
                return grant;
            }
            return new SocialCodeTokenGranter(
                    authenticationManager,
                    endpoints.getTokenServices(),
                    endpoints.getClientDetailsService(),
                    endpoints.getOAuth2RequestFactory()
            ).grant(grantType, tokenRequest);
        };
    }

    /**
     * 配置JWT的内容增强器, 加 tenant 字段到 jwt token中,
     * 返回json内容中添加user_id
     * {
     * "access_token": "tokenxxx", // tenant在生成的token中
     * "token_type": "bearer",
     * "expires_in": 43187,
     * "scope": "all",
     * "user_id": "20", // 显示 user_id
     * }
     */
    public TokenEnhancer tokenEnhancer() {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtAccessTokenConverter());
        delegates.add((accessToken, authentication) -> {
            if (authentication.getPrincipal() instanceof AuthUser) {
                AuthUser principal = (AuthUser) authentication.getPrincipal();
                Map<String, Object> info = new HashMap<>();
                info.put("user_id", principal.getUsername());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
            }
            return accessToken;
        });
        enhancerChain.setTokenEnhancers(delegates);
        return enhancerChain;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) jwtAccessTokenConverter
                .getAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new JwtUserAuthenticationConverter());
        jwtAccessTokenConverter.setSigningKey(authorizationServerProperties.getJwt().getKeyValue());
        return jwtAccessTokenConverter;
    }
}
