package cn.flizi.auth.security.social;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SocialDetailsService {

    UserDetails loadUserBySocial(String type, String code, String redirectUri) throws UsernameNotFoundException;

}
