package cn.flizi.auth.resource.demo.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class RestApiController {

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
        return result;
    }
}