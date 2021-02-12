package cn.flizi.auth.security.filter;

import cn.flizi.auth.security.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaValidationFilter extends OncePerRequestFilter {

    private final AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher("/login", "POST");

    private final AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler("/login?error");

    @Autowired
    private CaptchaService captchaCacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = antPathMatcher.matches(request);

        if (action) {
            try {
                validate(request);
            } catch (AuthenticationException e) {
                unsuccessfulAuthentication(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString(), failed);
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
            logger.debug("Delegating to authentication failure handler " + failureHandler);
        }


        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    private void validate(HttpServletRequest request) {
        String key = obtainKey(request);
        String code = obtainCode(request);

        if (!StringUtils.hasLength(key) || !StringUtils.hasLength(code)) {
            throw new BadCredentialsException("验证码不能为空");
        }

        String cacheCode = captchaCacheService.get(key);

        if (cacheCode == null) {
            throw new BadCredentialsException("验证码已失效");
        }

        if (!code.toLowerCase().equals(cacheCode)) {
            throw new BadCredentialsException("验证码错误");
        }
        captchaCacheService.delete(key);
    }

    private String obtainKey(HttpServletRequest request) {
        return request.getParameter("captchaKey");
    }

    private String obtainCode(HttpServletRequest request) {
        return request.getParameter("captchaCode");
    }
}
