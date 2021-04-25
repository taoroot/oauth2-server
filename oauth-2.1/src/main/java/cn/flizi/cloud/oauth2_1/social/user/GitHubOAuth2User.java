package cn.flizi.cloud.oauth2_1.social.user;

import cn.flizi.cloud.oauth2_1.social.user.CustomOAuth2User;
import lombok.Data;

@Data
public class GitHubOAuth2User implements CustomOAuth2User {

    public static final String TYPE = "github";

    private Map<String, Object> attributes;
    private String id;
    private String name;
    private String avatar_url;

    @Override
    public String getName() {
        return this.id;
    }

    @Override
    public String getNickname() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar_url;
    }
}