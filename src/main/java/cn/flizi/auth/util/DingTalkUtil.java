package cn.flizi.auth.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class DingTalkUtil implements ApplicationContextAware {
    private final static Executor executorPool = Executors.newSingleThreadExecutor();
    private static String access_token = "";

    public static final String DING_TOKEN_URL = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        access_token = applicationContext.getEnvironment().getProperty("dingtalk.access_token");
    }

    public static void sendTextAsync(String content) {
        executorPool.execute(() -> sendText(content));
    }

    public static void sendText(String content) {
        if (!StringUtils.hasLength(access_token) || access_token.length() < 10) {
            return;
        }
        RestTemplate httpsRestTemplate = new RestTemplate();
        DingTalkMsg dingTalkMsg = new DingTalkMsg();
        dingTalkMsg.setMsgtype("text");
        DingTalkMsg.TextBean textBean = new DingTalkMsg.TextBean();
        textBean.setContent("[AUTH]>" + content);
        dingTalkMsg.setText(textBean);

        DingTalkMsg.AtBean atBean = new DingTalkMsg.AtBean();
        atBean.setIsAtAll(Boolean.FALSE);
        dingTalkMsg.setAt(atBean);
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setContentType(MediaType.APPLICATION_JSON);

        System.out.println(dingTalkMsg);
        try {
            httpsRestTemplate.postForObject(
                    String.format(DING_TOKEN_URL, access_token),
                    new HttpEntity<>(dingTalkMsg, httpHeader),
                    Object.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }


    static class DingTalkMsg {

        private String msgtype;
        private TextBean text;
        private AtBean at;

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }

        public TextBean getText() {
            return text;
        }

        public void setText(TextBean text) {
            this.text = text;
        }

        public AtBean getAt() {
            return at;
        }

        public void setAt(AtBean at) {
            this.at = at;
        }

        public static class TextBean {
            private String content;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class AtBean {
            private boolean isAtAll;
            private List<String> atMobiles;

            public boolean isIsAtAll() {
                return isAtAll;
            }

            public void setIsAtAll(boolean isAtAll) {
                this.isAtAll = isAtAll;
            }

            public List<String> getAtMobiles() {
                return atMobiles;
            }

            public void setAtMobiles(List<String> atMobiles) {
                this.atMobiles = atMobiles;
            }
        }
    }
}
