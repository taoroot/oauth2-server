package cn.flizi.ext.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("sys_role_user")
@EqualsAndHashCode(callSuper = true)
public class SysRoleUser extends Model<SysRoleUser> {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer roleId;

    private Integer userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
