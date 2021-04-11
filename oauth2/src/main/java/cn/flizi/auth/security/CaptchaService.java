package cn.flizi.auth.security;

public interface CaptchaService {

    void set(String key, String value, long expiresInSeconds);

    boolean exists(String key);

    void delete(String key);

    String get(String key);

    default boolean check(String key, String value) {
        if (!exists(key)) {
            return false;
        }
        String s = get(key);
        boolean b = s != null && s.equalsIgnoreCase(value);
        if (b) {
            delete(key);
        }
        return b;
    }
}
