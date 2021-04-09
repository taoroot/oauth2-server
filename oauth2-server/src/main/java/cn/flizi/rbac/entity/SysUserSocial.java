package cn.flizi.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_user_social")
@EqualsAndHashCode(callSuper = true)
public class SysUserSocial extends Model<SysUserSocial> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer adminUserId;

    private String socialType;

    private String socialId;

    private String socialNickname;

    private String socialAvatar;

    @TableField(exist = false)
    private String icon;

    @TableField(exist = false)
    private String socialName;
}
