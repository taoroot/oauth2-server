package cn.flizi.auth.resource.demo;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决跨域问题
 */
public class CorsFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain arg2) throws IOException, ServletException {
        HttpServletRequest arg0 = (HttpServletRequest) servletRequest;
        HttpServletResponse arg1 = (HttpServletResponse) servletResponse;
        // 指定允许其他域名访问
        arg1.addHeader("Access-Control-Allow-Origin", "*");
        // 响应类型 响应方法
        arg1.addHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
        // 响应头设置
        arg1.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type");
        arg1.addHeader("Access-Control-Max-Age", "30");
        // 是否支持cookie跨域
        arg1.setHeader("Access-Control-Allow-Credentials", "true");
        // 需要过滤的代码
        arg2.doFilter(arg0, arg1);
    }
}