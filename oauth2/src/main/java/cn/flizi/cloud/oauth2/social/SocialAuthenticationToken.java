package cn.flizi.cloud.oauth2.social;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SocialAuthenticationToken extends AbstractAuthenticationToken {
    public SocialAuthenticationToken(String state, String code) {
        super(null);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
