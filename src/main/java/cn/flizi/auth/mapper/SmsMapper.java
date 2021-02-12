package cn.flizi.auth.mapper;

import cn.flizi.auth.entity.Sms;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SmsMapper {

    @Options(useGeneratedKeys = true, keyProperty = "smsId", keyColumn = "sms_id")
    @Insert("INSERT INTO sms (phone, code)" +
            "VALUES (#{phone},#{code})")
    void insert(Sms sms);

    @Select("select * from sms where phone=#{phone} order by create_time desc limit 1")
    Sms getSmsByPhone(String phone);
}