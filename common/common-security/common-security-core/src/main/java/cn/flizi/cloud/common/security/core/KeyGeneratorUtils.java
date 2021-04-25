package cn.flizi.cloud.common.security.core;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * @author Joe Grandja
 * @since 0.1.0
 */
public class KeyGeneratorUtils {

    public static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

}