package cn.flizi.auth.service;

import cn.flizi.auth.entity.Sms;
import cn.flizi.auth.entity.User;
import cn.flizi.auth.mapper.SmsMapper;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserService implements UserDetailsService, SocialDetailsService {
    private final UserMapper userMapper;
    private final SmsMapper smsMapper;
    private final SocialProperties socialProperties;

    public UserService(UserMapper userMapper, SmsMapper smsMapper, SocialProperties socialProperties) {
        this.userMapper = userMapper;
        this.smsMapper = smsMapper;
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

        // 短信登录
        if ("SMS".equals(type)) {
            user = smsHandler(code);
        }

        // 微信公众平台
        if ("WX_MP".equals(type)) {
            Map<String, String> map = wxMpHandler(code);
            String openId = map.get("openid");
            String unionId = map.get("unionid");
            if (unionId == null) {
                return null;
            }
            user = userMapper.loadUserByColumn("wx_unionid", unionId);
            if (user == null) {
                user = new User();
                user.setWxOpenid(openId);
                user.setWxUnionid(unionId);
                user.setPassword("{bcrypt}" + new BCryptPasswordEncoder()
                        .encode(UUID.randomUUID().toString().replace("_", "")));
                userMapper.insert(user);
            } else if (!StringUtils.hasLength(user.getWxOpenid())) {
                userMapper.updateWxOpenId(user.getUserId(), openId);
            }
        }

        // 微信开放平台
        if ("WX_OPEN".equals(type)) {
            String unionId = wxOpenHandler(code);
            user = userMapper.loadUserByColumn("wx_unionid", unionId);
            if (user == null) {
                user = new User();
                user.setWxUnionid(unionId);
                user.setPassword("{bcrypt}" + new BCryptPasswordEncoder()
                        .encode(UUID.randomUUID().toString().replace("_", "")));
                userMapper.insert(user);
            } else if (user.getWxOpenid() != null) {
                userMapper.updateWxOpenId(user.getUserId(), user.getWxOpenid());
            }
        }

        if (user != null) {
            return new AuthUser(user.getUserId(), AuthorityUtils.NO_AUTHORITIES);
        }

        return null;
    }

    private User smsHandler(String codeStr) {
        if (!codeStr.contains(":")) {
            return null;
        }

        String phone = codeStr.split(":")[0];
        String code = codeStr.split(":")[1];
        Sms sms = smsMapper.getCodeByPhone(phone);
        if (sms == null || !sms.getCode().equals(code)
                || new Date().getTime() - sms.getCreateTime().getTime() > 60 * 1000) {
            return null;
        }

        return userMapper.loadUserByColumn("phone", phone);
    }

    public String wxOpenHandler(String code) {
        String uri = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                socialProperties.getWxOpen().getKey(),
                socialProperties.getWxOpen().getSecret(),
                code);
        Map<String, Object> map = getStringObjectMap(uri);
        return (String) map.get("unionid");
    }

    public Map<String, String> wxMpHandler(String code) {
        String uri = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                socialProperties.getWxMp().getKey(),
                socialProperties.getWxMp().getSecret(),
                code);
        Map<String, Object> map = getStringObjectMap(uri);
        String openId = (String) map.get("openid");
        String unionId = (String) map.get("unionid");

        Map<String, String> result = new HashMap<>();
        result.put("openid", openId);
        result.put("unionid", unionId);
        return result;
    }

    public Map<String, Object> getStringObjectMap(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .getBody();
    }

    private static class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        public WxMappingJackson2HttpMessageConverter() {
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.TEXT_PLAIN);
            mediaTypes.add(MediaType.TEXT_HTML);  // 解决微信问题:  放回格式是 text/plain 的问题
            setSupportedMediaTypes(mediaTypes);
        }
    }
}
