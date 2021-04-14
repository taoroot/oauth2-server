package cn.flizi.auth.config;

import cn.flizi.auth.security.AuthUser;
import cn.flizi.auth.security.CaptchaService;
import cn.flizi.auth.security.CustomWebResponseExceptionTranslator;
import cn.flizi.auth.security.JwtUserAuthenticationConverter;
import cn.flizi.auth.security.social.SocialCodeTokenGranter;
import cn.flizi.auth.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jwt.crypto.sign.MacSigner;
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
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Security Security OAuth 配置
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final DataSource dataSource;

    private final AuthorizationServerProperties authorizationServerProperties;

    private final CaptchaService captchaService;

    public AuthorizationConfig(AuthenticationManager authenticationManager,
                               DataSource dataSource,
                               UserService userDetailsService, AuthorizationServerProperties authorizationServerProperties, CaptchaService captchaService) {
        this.authenticationManager = authenticationManager;
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
        this.authorizationServerProperties = authorizationServerProperties;
        this.captchaService = captchaService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.authenticationEntryPoint(AuthorizationConfig::commence);
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
        delegates.add((accessToken, authentication) -> {
            if (authentication.getPrincipal() instanceof AuthUser) {
                AuthUser principal = (AuthUser) authentication.getPrincipal();
                Map<String, Object> info = new HashMap<>();
                info.put("user_id", principal.getUsername());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
            }
            return accessToken;
        });
        delegates.add(jwtAccessTokenConverter());
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

    private static void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (!response.isCommitted()) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);

            HashMap<String, Object> result = new HashMap<>();
            result.put("code", "FAIL");
            result.put("msg", authException.getMessage());
            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        }
    }
}
