package cn.flizi.auth.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {
    private Boolean enable = true;
    private Integer length = 4;
    private String baseStr = "023456789";

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getBaseStr() {
        return baseStr;
    }

    public void setBaseStr(String baseStr) {
        this.baseStr = baseStr;
    }

    @Override
    public String toString() {
        return "CaptchaProperties{" +
                "enable=" + enable +
                ", length=" + length +
                ", baseStr='" + baseStr + '\'' +
                '}';
    }
}
