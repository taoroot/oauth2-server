package cn.flizi.auth.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "social")
public class SocialProperties {

    private WxMp wxMp = new WxMp();
    private WxOpen wxOpen = new WxOpen();

    public WxMp getWxMp() {
        return wxMp;
    }

    public void setWxMp(WxMp wxMp) {
        this.wxMp = wxMp;
    }

    public WxOpen getWxOpen() {
        return wxOpen;
    }

    public void setWxOpen(WxOpen wxOpen) {
        this.wxOpen = wxOpen;
    }

    public static class WxMp {
        private String key;
        private String secret;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        @Override
        public String toString() {
            return "WxOpen{" +
                    "key='" + key + '\'' +
                    ", secret='" + secret + '\'' +
                    '}';
        }
    }

    public static class WxOpen {
        private String key;
        private String secret;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        @Override
        public String toString() {
            return "WxOpen{" +
                    "key='" + key + '\'' +
                    ", secret='" + secret + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SocialProperties{" +
                "wxMp=" + wxMp +
                ", wxOpen=" + wxOpen +
                '}';
    }
}
