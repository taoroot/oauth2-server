package cn.flizi.auth.mapper;

import cn.flizi.auth.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE phone = #{name} or email = #{name}")
    User loadUserByUsername(String name);

    @Select("SELECT * FROM user WHERE `user_id` = #{userId}")
    User loadUserByUserId(String userId);

    @Select("SELECT * FROM user WHERE ${column} = #{value}")
    User loadUserByColumn(String column, String value);

    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    @Insert("INSERT INTO user (password, phone, email, wx_openid, wx_unionid)" +
            "VALUES (#{password},#{phone},#{email}, #{wxOpenid}, #{wxUnionid})")
    void insert(User user);

    @Update("update user set password=#{password} where phone=#{phone}")
    void updatePassword(String phone, String password);
}