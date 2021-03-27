package cn.flizi.auth.mapper;

import cn.flizi.auth.entity.Captcha;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CaptchaMapper {

    @Options(useGeneratedKeys = true, keyProperty = "captchaId", keyColumn = "captcha_id")
    @Insert("INSERT INTO captcha (`key`, code) VALUES (#{key}, #{code})")
    void insert(Captcha captcha);

    @Select("SELECT * FROM captcha WHERE `key`=#{key} ORDER BY create_time DESC LIMIT 1")
    Captcha getCaptchaByKey(String key);

    @Delete("DELETE FROM captcha WHERE `key`=#{key}")
    void delete(String key);
}
