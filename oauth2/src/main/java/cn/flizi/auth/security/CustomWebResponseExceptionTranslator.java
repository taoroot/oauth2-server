package cn.flizi.auth.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", "ERROR");
        result.put("msg", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
