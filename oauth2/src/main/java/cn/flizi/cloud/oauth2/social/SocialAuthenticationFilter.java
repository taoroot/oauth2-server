package cn.flizi.cloud.oauth2.social;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected SocialAuthenticationFilter() {
        super(new AntPathRequestMatcher("/social/callback/**"));
    }


    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String state = obtainCode(request);
        String code = obtainState(request);

        if (state == null) {
            state = "";
        }

        if (code == null) {
            code = "";
        }

        state = state.trim();
        code = code.trim();

        SocialAuthenticationToken authRequest = new SocialAuthenticationToken(state, code);

        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainState(HttpServletRequest request) {
        return request.getParameter("state");
    }

    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter("code");
    }
}