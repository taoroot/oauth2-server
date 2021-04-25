package cn.flizi.cloud.oauth2.social.impl;

import cn.flizi.cloud.common.security.core.social.SocialClient;
import cn.flizi.cloud.oauth2.social.SocialDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultSocialDetailsService implements SocialDetailsService {
    @Override
    public UserDetails loadUserBySocial(String type, String code, String state, String redirectUri) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails bind(String username, String socialId) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public SocialClient getClient(String type, String appId) {
        return null;
    }
}
