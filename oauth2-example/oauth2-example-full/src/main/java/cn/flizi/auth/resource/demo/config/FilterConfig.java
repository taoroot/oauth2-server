package cn.flizi.auth.resource.demo.config;

import cn.flizi.auth.resource.demo.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class FilterConfig {

    @Bean
    @Order(Integer.MAX_VALUE -2)
    public FilterRegistrationBean CorsFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.addUrlPatterns("*");
        return registrationBean;
    }
}