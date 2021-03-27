package cn.flizi.auth.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {
    private Boolean enable = true;
    private String secretId;
    private String secretKey;
    private String appId;
    private String sign;
    private String templateId;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Override
    public String toString() {
        return "SmsProperties{" +
                "enable=" + enable +
                ", secretId='" + secretId + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", appId='" + appId + '\'' +
                ", sign='" + sign + '\'' +
                ", templateId='" + templateId + '\'' +
                '}';
    }
}
