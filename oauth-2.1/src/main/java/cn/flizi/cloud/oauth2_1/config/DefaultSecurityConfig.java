package cn.flizi.cloud.oauth2_1.config;

import cn.flizi.cloud.oauth2_1.social.converter.CustomMapOAuth2AccessTokenResponseConverter;
import cn.flizi.cloud.oauth2_1.social.converter.CustomOAuth2AuthorizationCodeGrantRequestEntityConverter;
import cn.flizi.cloud.oauth2_1.social.converter.CustomOAuth2AuthorizationRequestResolver;
import cn.flizi.cloud.oauth2_1.social.user.impl.GitHubOAuth2User;
import cn.flizi.cloud.oauth2_1.social.user.impl.GiteeOAuth2User;
import cn.flizi.cloud.oauth2_1.social.user.impl.QQOAuth2User;
import cn.flizi.cloud.oauth2_1.social.user.impl.WechatOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DefaultSecurityConfig {

    // formatter:off
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
    // formatter:on

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;


    // 解决第三方授权登录兼容性问题
    private void authorizationEndpoint(OAuth2LoginConfigurer<HttpSecurity>.AuthorizationEndpointConfig authorization) {
        authorization.authorizationRequestResolver(new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI));
    }

    // 解决第三方授权登录兼容性问题
    private void tokenEndpoint(OAuth2LoginConfigurer<HttpSecurity>.TokenEndpointConfig tokenEndpoint) {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRequestEntityConverter(new CustomOAuth2AuthorizationCodeGrantRequestEntityConverter());
        OAuth2AccessTokenResponseHttpMessageConverter oAuth2AccessTokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        oAuth2AccessTokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomMapOAuth2AccessTokenResponseConverter());
        ArrayList<MediaType> mediaTypes = new ArrayList<>(oAuth2AccessTokenResponseHttpMessageConverter.getSupportedMediaTypes());
        mediaTypes.add(MediaType.TEXT_PLAIN); // 解决微信问题:  放回是text/plain 的问题
        mediaTypes.add(MediaType.TEXT_HTML); // 解决QQ问题:  放回是text/html 的问题
        oAuth2AccessTokenResponseHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), oAuth2AccessTokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        client.setRestOperations(restTemplate);
        tokenEndpoint.accessTokenResponseClient(client);
    }

    /**
     * 1. 解决第三方授权登录兼容性问题
     * 2. 加载平台 userId
     */
    private void userInfoEndpoint(OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig userInfo) {
        // 目前支持这四个, 如果是标准 OAuth2.0 协议,只需加入自定义的OAuth2User就好
        Map<String, Class<? extends OAuth2User>> customUserTypes = new HashMap<>();
        customUserTypes.put(GiteeOAuth2User.TYPE, GiteeOAuth2User.class);
        customUserTypes.put(WechatOAuth2User.TYPE, WechatOAuth2User.class);
        customUserTypes.put(QQOAuth2User.TYPE, QQOAuth2User.class);
        customUserTypes.put(GitHubOAuth2User.TYPE, GitHubOAuth2User.class);
//        CustomUserTypesOAuth2UserService customOAuth2UserService = new UserDetailTypeOAuthUserService(socialDetailsService, customUserTypes);
//        customOAuth2UserService.setRequestEntityConverter(new CustomOAuth2UserRequestEntityConverter());
//
//        RestTemplate restTemplate = new RestTemplate(); // 解决微信问题: 放回是text/plain 的问题
//        restTemplate.getMessageConverters().add(new CustomMappingJackson2HttpMessageConverter());
//        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
//        customOAuth2UserService.setRestOperations(restTemplate);

//        userInfo.userService(customOAuth2UserService);
    }

}