package cn.flizi.auth;

import cn.flizi.auth.properties.CaptchaProperties;
import cn.flizi.auth.properties.SmsProperties;
import cn.flizi.auth.properties.SocialProperties;
import cn.flizi.auth.security.social.SocialAuthenticationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthApplication implements CommandLineRunner {
    private static final Logger log = LogManager.getLogger(SocialAuthenticationProvider.class);

    @Autowired
    private CaptchaProperties captchaProperties;
    @Autowired
    private SocialProperties socialProperties;
    @Autowired
    private SmsProperties smsProperties;

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info(captchaProperties);
        log.info(socialProperties);
        log.info(smsProperties);
    }
}
