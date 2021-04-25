package cn.flizi.cloud.common.security.core.social;

import javax.servlet.http.HttpServletRequest;

public class SocialClient {
    public SocialToken getToken(HttpServletRequest request) {
        return null;
    }

    public SocialUserInfo getUserInfo(SocialToken socialToken) {
        return null;
    }

    public SocialUserInfo getUserInfo(HttpServletRequest request) {
        SocialToken token = getToken(request);
        return getUserInfo(token);
    }
}
