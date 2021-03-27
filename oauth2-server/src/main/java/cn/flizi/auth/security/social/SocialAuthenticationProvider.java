package cn.flizi.auth.security.social;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 手机验证码登录、社交登录
 * 注意,社交账号返回的code是指向一个用户的, 手机号返回的验证码则不是,因此手机号的code应该存储为: 手机号_验证码
 */
public class SocialAuthenticationProvider implements AuthenticationProvider {
    private static final Logger log = LogManager.getLogger(SocialAuthenticationProvider.class);
    protected static MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private static final UserDetailsChecker detailsChecker = new DefaultPreAuthenticationChecks();

    private final SocialDetailsService socialDetailsService;

    public SocialAuthenticationProvider(SocialDetailsService socialDetailsService) {
        this.socialDetailsService = socialDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        SocialCodeAuthenticationToken socialCodeAuthenticationToken = (SocialCodeAuthenticationToken) authentication;

        Object type = socialCodeAuthenticationToken.getPrincipal();
        Object code = socialCodeAuthenticationToken.getCredentials();
        String redirectUri = socialCodeAuthenticationToken.getRedirectUri();

        if (type == null || code == null) {
            log.debug("Authentication failed: no type or code provided");
            throw new BadCredentialsException("type and code must be required");
        }

        UserDetails userDetails = socialDetailsService.loadUserBySocial(type.toString(),
                code.toString(), redirectUri);
        if (userDetails == null) {
            log.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.noopBindAccount", "Noop Bind Account"));
        }

        detailsChecker.check(userDetails);

        SocialCodeAuthenticationToken authenticationToken = new SocialCodeAuthenticationToken(
                userDetails, type, userDetails.getAuthorities());
        authenticationToken.setDetails(socialCodeAuthenticationToken.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SocialCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                log.debug("User account is locked");

                throw new LockedException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"));
            }

            if (!user.isEnabled()) {
                log.debug("User account is disabled");

                throw new DisabledException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"));
            }

            if (!user.isAccountNonExpired()) {
                log.debug("User account is expired");

                throw new AccountExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"));
            }
        }
    }
}
