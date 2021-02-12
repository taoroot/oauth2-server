package cn.flizi.auth.service;

import cn.flizi.auth.entity.User;
import cn.flizi.auth.mapper.UserMapper;
import cn.flizi.auth.properties.SocialProperties;
import cn.flizi.auth.security.AuthUser;
import cn.flizi.auth.security.social.SocialDetailsService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService, SocialDetailsService {
    private final UserMapper userMapper;
    private final SocialProperties socialProperties;

    public UserService(UserMapper userMapper, SocialProperties socialProperties) {
        this.userMapper = userMapper;
        this.socialProperties = socialProperties;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("NOT_FOUND_USER");
        }

        return new AuthUser(
                user.getUserId(),
                user.getPassword(),
                true,
                AuthorityUtils.NO_AUTHORITIES
        );
    }

    @Override
    public UserDetails loadUserBySocial(String type, String code, String redirectUri) throws UsernameNotFoundException {
        User user = null;

        if ("WX_MP".equals(type)) {
            String uri = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                    socialProperties.getWxMp().getKey(),
                    socialProperties.getWxMp().getSecret(),
                    code);
            Map<String, Object> map = getStringObjectMap(uri);
            String openId = (String) map.get("openid");

            String token = getToken();
            uri = String.format("https://api.weixin.qq.com/cgi-bin/user/info?openid=%s&lang=zh_CN&access_token=%s",
                    openId,
                    token);
            map = getStringObjectMap(uri);
            String unionId = (String) map.get("unionid");

            user = userMapper.loadUserByColumn("wx_unionid", unionId);
            if (user == null) {
                user = new User();
                user.setWxOpenid(openId);
                user.setWxUnionid(unionId);
                userMapper.insert(user);
            }
        }

        if ("WX_OPEN".equals(type)) {
            String uri = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                    socialProperties.getWxOpen().getKey(),
                    socialProperties.getWxOpen().getSecret(),
                    code);
            Map<String, Object> map = getStringObjectMap(uri);
            String openId = (String) map.get("openid");
            String unionId = (String) map.get("unionid");

            user = userMapper.loadUserByColumn("wx_unionid", unionId);
            if (user == null) {
                user = new User();
                user.setWxUnionid(unionId);
                userMapper.insert(user);
            }
        }

        if (user != null) {
            return new AuthUser(user.getUserId(), AuthorityUtils.NO_AUTHORITIES);
        }

        return null;
    }

    private static class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        public WxMappingJackson2HttpMessageConverter() {
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.TEXT_PLAIN);
            mediaTypes.add(MediaType.TEXT_HTML);  // 解决微信问题:  放回格式是 text/plain 的问题
            setSupportedMediaTypes(mediaTypes);
        }
    }


    private static String token = null;
    private static Long oldDate = null;
    public static final long HOUR = 3500 * 1000 * 2;

    private String getToken() {
        long now = new Date().getTime();
        if (oldDate == null || now - oldDate > HOUR) {
            String uri = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                    socialProperties.getWxMp().getKey(),
                    socialProperties.getWxMp().getSecret());
            Map<String, Object> stringObjectMap = getStringObjectMap(uri);
            token = (String) stringObjectMap.get("access_token");
            oldDate = now;
        }
        return token;
    }


    private Map<String, Object> getStringObjectMap(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .getBody();
    }
}
