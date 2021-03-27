package cn.flizi.auth.security.social;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

public class SocialCodeAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    // ~ Instance fields
    // ================================================================================================

    private final Object type;
    private Object code;

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    private String redirectUri;

    // ~ Constructors
    // ===================================================================================================
    public SocialCodeAuthenticationToken(String type, String code, String redirectUri) {
        super(null);
        this.type = type;
        this.code = code;
        this.redirectUri = redirectUri;
        setAuthenticated(false);
    }
    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     */
    public SocialCodeAuthenticationToken(Object type, Object code) {
        super(null);
        this.type = type;
        this.code = code;
        setAuthenticated(false);
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param type
     * @param code
     * @param authorities
     */
    public SocialCodeAuthenticationToken(Object type, Object code, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.type = type;
        this.code = code;
        super.setAuthenticated(true); // must use super, as we override
    }

    // ~ Methods
    // ========================================================================================================

    public Object getCredentials() {
        return this.code;
    }

    public Object getPrincipal() {
        return this.type;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        code = null;
    }
}