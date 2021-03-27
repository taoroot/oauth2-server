package cn.flizi.auth.client.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.oauth2.client")
public class ClientProperties {

    private String clientId;
    private String clientSecret;
    private String tokenInfoUri;
}
