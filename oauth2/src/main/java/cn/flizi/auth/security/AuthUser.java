package cn.flizi.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Security 的 User 类 和 用户表 User 类重名, 这里集成一次以作区分
 * Security 的 User 类 username 对应 用户表 user_id 字段
 * 扩展 字段 phone
 */
public class AuthUser extends User {

    private String phone;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public AuthUser(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
    }

    public AuthUser(String username, Collection<? extends GrantedAuthority> authorities) {
        super(username, "N/A", true, true, true, true, authorities);
    }
}
