package cn.flizi.cloud.oauth2.sms;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SmsDetailsService {

    UserDetails loadUserByPhone(String phone, String code) throws UsernameNotFoundException;

}