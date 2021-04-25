package cn.flizi.cloud.oauth2.sms.impl;

import cn.flizi.cloud.oauth2.sms.SmsDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class DefaultSmsDetailsService implements SmsDetailsService {
    @Override
    public UserDetails loadUserByPhone(String phone, String code) throws UsernameNotFoundException {
        return null;
    }
}
