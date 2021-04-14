package cn.flizi.auth.config;

import cn.flizi.auth.security.JwtUserAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@EnableResourceServer
@SuppressWarnings("deprecation")
@Configuration
public class ResourceConfig extends ResourceServerConfigurerAdapter implements JwtAccessTokenConverterConfigurer {

    @Autowired
    private AuthorizationServerProperties serverProperties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/user_base", "/user_info", "/api/**");
        super.configure(http);
    }

    @Override
    public void configure(JwtAccessTokenConverter jwtAccessTokenConverter) {
        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) jwtAccessTokenConverter
                .getAccessTokenConverter();
        JwtUserAuthenticationConverter authUserAuthenticationConverter = new JwtUserAuthenticationConverter();

        accessTokenConverter.setUserTokenConverter(authUserAuthenticationConverter);
        jwtAccessTokenConverter.setSigningKey(serverProperties.getJwt().getKeyValue());  // 配置JWT使用的秘钥
    }
}