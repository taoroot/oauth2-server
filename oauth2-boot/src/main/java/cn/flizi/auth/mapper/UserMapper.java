package cn.flizi.auth.mapper;

import cn.flizi.auth.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE phone = #{name} or email = #{name}")
    User loadUserByUsername(String name);

    @Select("SELECT * FROM user WHERE `user_id` = #{userId}")
    User loadUserByUserId(String userId);

    @Select("SELECT * FROM user WHERE ${column} = #{value}")
    User loadUserByColumn(@Param("column") String column, @Param("value") String value);

    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    @Insert("INSERT INTO user (password, phone, email, wx_openid, wx_unionid)" +
            "VALUES (#{password},#{phone},#{email}, #{wxOpenid}, #{wxUnionid})")
    void insert(User user);

    @Update("update user set password=#{password} where phone=#{phone}")
    void updatePassword(@Param("phone") String phone, @Param("password") String password);

    @Update("update user set phone=#{phone} where `user_id`=#{userId}")
    void updatePhone(@Param("userId") String userId, @Param("phone") String phone);

    @Update("update user set wx_openid=#{openid} where `user_id`=#{userId}")
    void updateWxOpenId(@Param("userId") String userId, @Param("openid") String openid);

    @Update("update user set wx_unionid=#{unionid} where `user_id`=#{userId}")
    void updateWxUnionId(@Param("userId") String userId, @Param("unionid") String unionid);
}