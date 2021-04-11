package cn.flizi.auth.client.web;

import cn.flizi.auth.client.properties.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RestApiController {

    @Autowired
    private ClientProperties clientProperties;

    @PostMapping(value = "token")
    public Map<String, Object> userBase(@RequestParam Map<String, String> params) {
        HashMap<String, Object> result = new HashMap<>();
        String code = params.get("code");
        if (!StringUtils.hasLength(code)) {
            result.put("code", "ERROR");
            result.put("msg", "无效CODE");
            return result;
        }

        String uri = String.format(clientProperties.getTokenInfoUri(),
                clientProperties.getClientId(),
                clientProperties.getClientSecret(),
                code);
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