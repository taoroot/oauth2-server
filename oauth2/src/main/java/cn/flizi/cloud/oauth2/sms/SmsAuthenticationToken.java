package cn.flizi.cloud.oauth2.sms;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Getter
    private final String phone;
    @Getter
    private final String code;

    public SmsAuthenticationToken(String phone, String code) {
        super(null);
        this.phone = phone;
        this.code = code;
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }
}