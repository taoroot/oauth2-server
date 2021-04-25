package cn.flizi.cloud.oauth2.sms;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final String typeParameter = "phone";
    private final String codeParameter = "code";

    public SmsAuthenticationFilter() {
        super(new AntPathRequestMatcher("/sms", "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String type = obtainUsername(request);
        String code = obtainPassword(request);

        if (type == null) {
            type = "";
        }

        if (code == null) {
            code = "";
        }

        type = type.trim();
        code = code.trim();

        SmsAuthenticationToken authRequest = new SmsAuthenticationToken(type, code);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(typeParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              SmsAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}