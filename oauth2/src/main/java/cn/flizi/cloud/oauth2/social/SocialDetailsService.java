package cn.flizi.cloud.oauth2.social;

import cn.flizi.cloud.common.security.core.social.SocialClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SocialDetailsService {

    UserDetails loadUserBySocial(String type, String code, String state, String redirectUri) throws UsernameNotFoundException;

    UserDetails bind(String username, String socialId) throws UsernameNotFoundException;

    SocialClient getClient(String type, String appId);
}