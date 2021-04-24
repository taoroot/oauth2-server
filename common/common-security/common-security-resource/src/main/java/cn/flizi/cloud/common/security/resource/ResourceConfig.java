package cn.flizi.cloud.common.security.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Component
public class ResourceConfig extends WebSecurityConfigurerAdapter {

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationEntryPoint authenticationEntryPoint = new AuthenticationEntryPoint();

        http
                // JWT登录
                .oauth2ResourceServer(config ->
                        config
                                // 处理 AccessDeniedException
                                .authenticationEntryPoint(authenticationEntryPoint)

                                .bearerTokenResolver(bearerTokenResolver())
                                .jwt()
                                // 将jwt内容转为Security Authentication
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))

                // 跨域
                .cors(config -> config.configurationSource(req -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
                    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                    return corsConfiguration;
                }))
                // 禁用CSRF
                .csrf().disable()
                // 禁用 SESSION
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests((requests) -> requests.anyRequest().authenticated());
    }

    /**
     *
     * @return
     */
    @Bean
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }


    /**
     * 从参数中获取  BearerToken
     */
    @Bean
    private DefaultBearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
        // 允许 表单内容的 access_token 字段传递
        defaultBearerTokenResolver.setAllowFormEncodedBodyParameter(true);
        // 允许 参数传递 http://url:port?access_token={BearerToken}
        defaultBearerTokenResolver.setAllowUriQueryParameter(true);
        return defaultBearerTokenResolver;
    }
}