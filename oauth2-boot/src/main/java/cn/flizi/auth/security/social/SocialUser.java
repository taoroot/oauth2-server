package cn.flizi.auth.security.social;

import org.springframework.security.core.AuthenticatedPrincipal;

public class SocialUser implements AuthenticatedPrincipal {

    private String accessToken;

    private String username;

    private String nickname;

    private String avatar;

    @Override
    public String getName() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
