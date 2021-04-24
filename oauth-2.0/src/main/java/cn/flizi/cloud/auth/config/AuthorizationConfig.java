package cn.flizi.cloud.auth.config;

import cn.flizi.cloud.auth.security.CustomWebResponseExceptionTranslator;
import cn.flizi.cloud.auth.security.JwtUserAuthenticationConverter;
import cn.flizi.cloud.auth.security.RestAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Security Security OAuth 配置
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
@AllArgsConstructor
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final DataSource dataSource;

    private final AuthorizationServerProperties authorizationServerProperties;

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
                .authenticationManager(authenticationManager);   // password 模式, 验证密码, 返回用户登录信息
        // @formatter:on
    }

    /**
     * 配置客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // @formatter:off
        clients.inMemory()
                .withClient("client")
                .authorizedGrantTypes("authorization_code", "password")
                .scopes("read", "write")
                .redirectUris("http://localhost:8080/")
                .secret("{noop}secret"); // 密码不加密
        // @formatter:on
    }

    /**
     * 扩展支持 social 模式, 并且 password 模式添加验证码
     */
    public TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints,
                                     AuthenticationManager authenticationManager) {
        return endpoints.getTokenGranter();
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
