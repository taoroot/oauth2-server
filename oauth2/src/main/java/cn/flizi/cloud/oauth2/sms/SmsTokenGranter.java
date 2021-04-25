package cn.flizi.cloud.oauth2.sms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;


public class SmsTokenGranter implements AuthenticationProvider {

    private final Log logger = LogFactory.getLog(getClass());
    Map<String, SmsDetailsService> detailsServiceMap;

    public SmsTokenGranter(Map<String, SmsDetailsService> map) {
        detailsServiceMap = map;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken) authentication;
        String phone = (String) smsAuthenticationToken.getPrincipal();
        String code = (String) smsAuthenticationToken.getCredentials();
        SmsDetailsService socialCodeHandler = detailsServiceMap.get(phone);

        UserDetails userDetails = socialCodeHandler.loadUserByPhone(phone, code);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), smsAuthenticationToken, userDetails.getAuthorities()
        );
        usernamePasswordAuthenticationToken.setDetails(authentication.getDetails());
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}