package cn.flizi.auth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Map;

/**
 * 生成 JWT 的时候加入 tenant 字段, 解析jwt的时候, 将 tenant放入 Authentication中
 */
@SuppressWarnings("deprecation")
public class JwtUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    public static final String TENANT_ID = "tenant";


    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = (Map<String, Object>) super.convertUserAuthentication(authentication);
        response.put(TENANT_ID, 1);
        return response;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        Authentication authentication = super.extractAuthentication(map);
        if (authentication != null) {
            String userId = authentication.getName();
            Integer tenantId = (Integer) map.get(TENANT_ID);
            AuthUser principal = new AuthUser(userId, tenantId, authentication.getAuthorities());
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authentication.getAuthorities());
        }
        return null;
    }

}
