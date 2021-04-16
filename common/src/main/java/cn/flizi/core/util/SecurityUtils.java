package cn.flizi.core.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Integer userId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(name)) {
            return -1;
        }
        return Integer.parseInt(name);
    }
}
