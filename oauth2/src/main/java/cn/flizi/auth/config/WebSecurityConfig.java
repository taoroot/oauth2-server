package cn.flizi.auth.config;

import cn.flizi.auth.security.filter.CaptchaValidationFilter;
import cn.flizi.auth.security.social.SocialAuthenticationProvider;
import cn.flizi.auth.security.social.SocialCodeAuthenticationFilter;
import cn.flizi.auth.security.social.SocialDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SocialDetailsService socialDetailsService;
    @Autowired
    private CaptchaValidationFilter captchaValidationFilter;

    /**
     * 注入 Spring 容器中, 在授权服务器中密码模式下,验证用户密码正确性, 以及第三方授权码验证
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new SocialAuthenticationProvider(socialDetailsService));
        auth.userDetailsService(userDetailsService);
    }

    /**
     * 配置 HttpSecurity
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SocialCodeAuthenticationFilter socialCodeAuthenticationFilter = new SocialCodeAuthenticationFilter();
        socialCodeAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        http
                .csrf().disable()
                .formLogin()
                .loginPage("/login").and()
                .authorizeRequests()
                // 对外开放接口
                .antMatchers("/login", "/social", "/reset", "/captcha", "/signup",
                        "/sms", "/auth-redirect", "/weixin-code").permitAll()
                // 静态资源
                .antMatchers("/static/**").permitAll()
                // swagger api
                .antMatchers("/v2/api-docs", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(captchaValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(socialCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic();
    }

}
