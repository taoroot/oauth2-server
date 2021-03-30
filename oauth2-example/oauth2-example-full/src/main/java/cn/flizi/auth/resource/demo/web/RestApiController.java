package cn.flizi.auth.resource.demo.web;

import cn.flizi.auth.resource.demo.properties.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestApiController {

    @Autowired
    private ClientProperties clientProperties;

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping(value = "/")
    public HashMap<String, Object> userBase() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", "SUCCESS");
        result.put("msg", "资源服务器(test)");
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", userId);
        result.put("data", data);
        result.put("session", sessionRegistry.getAllPrincipals());
        return result;
    }

    @GetMapping(value = "/user_info")
    public HashMap<String, Object> checkToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", "SUCCESS");
        result.put("msg", "资源服务器(test)");
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", userId);
        result.put("data", data);
        result.put("session", sessionRegistry.getAllPrincipals());
        return result;
    }

    @PostMapping(value = "token")
    public Map<String, Object> userBase(@RequestParam Map<String, String> params) {
        HashMap<String, Object> result = new HashMap<>();
        String code = params.get("code");
        if (!StringUtils.hasLength(code)) {
            result.put("code", "ERROR");
            result.put("msg", "无效CODE");
            return result;
        }
        String redirectUri = params.get("redirect_uri");
        if (!StringUtils.hasLength(redirectUri)) {
            result.put("code", "ERROR");
            result.put("msg", "无效 redirectUri");
            return result;
        }

        String uri = String.format(clientProperties.getTokenInfoUri(),
                clientProperties.getClientId(),
                clientProperties.getClientSecret(),
                code, redirectUri
                );
        return getStringObjectMap(uri);
    }

    public Map<String, Object> getStringObjectMap(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .getBody();
    }
}