package cn.flizi.cloud.auth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Controller
class JwkSetEndpoint {
    KeyPair keyPair;

    public JwkSetEndpoint(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @GetMapping("/oauth2/jwks")
    @ResponseBody
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

    @GetMapping("/.well-known/openid-configuration")
    @ResponseBody
    public String openId() {
        return "{\n" +
                "\"issuer\": \"http://auth-server:9000\",\n" +
                "\"authorization_endpoint\": \"http://auth-server:9000/oauth2/authorize\",\n" +
                "\"token_endpoint\": \"http://auth-server:9000/oauth2/token\",\n" +
                "\"token_endpoint_auth_methods_supported\": [\n" +
                "\"client_secret_basic\",\n" +
                "\"client_secret_post\"\n" +
                "],\n" +
                "\"jwks_uri\": \"http://auth-server:9000/oauth2/jwks\",\n" +
                "\"response_types_supported\": [\n" +
                "\"code\"\n" +
                "],\n" +
                "\"grant_types_supported\": [\n" +
                "\"authorization_code\",\n" +
                "\"client_credentials\",\n" +
                "\"refresh_token\"\n" +
                "],\n" +
                "\"subject_types_supported\": [\n" +
                "\"public\"\n" +
                "],\n" +
                "\"id_token_signing_alg_values_supported\": [\n" +
                "\"RS256\"\n" +
                "],\n" +
                "\"scopes_supported\": [\n" +
                "\"openid\"\n" +
                "]\n" +
                "}";
    }
}