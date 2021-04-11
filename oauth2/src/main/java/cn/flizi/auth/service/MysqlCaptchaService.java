package cn.flizi.auth.service;

import cn.flizi.auth.entity.Captcha;
import cn.flizi.auth.mapper.CaptchaMapper;
import cn.flizi.auth.security.CaptchaService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class MysqlCaptchaService implements CaptchaService {
    private final CaptchaMapper captchaMapper;

    public MysqlCaptchaService(CaptchaMapper captchaMapper) {
        this.captchaMapper = captchaMapper;
    }

    @Override
    public void set(String key, String value, long expiresInSeconds) {
        Captcha captcha = new Captcha();
        captcha.setKey(key);
        captcha.setCode(value);
        captchaMapper.insert(captcha);
    }

    @Override
    public boolean exists(String key) {
        if (!StringUtils.hasLength(key)) {
            return false;
        }
        Captcha captcha = captchaMapper.getCaptchaByKey(key);
        if (captcha == null) {
            return false;
        }
        long now = new Date().getTime();
        if ((now - captcha.getCreateTime().getTime()) > 60 * 1000) {
            return false;
        }
        return true;
    }

    @Override
    public void delete(String key) {
        captchaMapper.delete(key);
    }

    @Override
    public String get(String key) {
        Captcha captchaByKey = captchaMapper.getCaptchaByKey(key);
        return captchaByKey.getCode();
    }
}
