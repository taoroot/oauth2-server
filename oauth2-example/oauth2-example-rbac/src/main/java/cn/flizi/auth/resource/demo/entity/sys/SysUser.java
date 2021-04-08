package cn.flizi.auth.resource.demo.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : zhiyi
 * Date: 2020/2/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
@NoArgsConstructor
@AllArgsConstructor
public class SysUser extends Model<SysUser> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String phone;

    private String avatar;

    private String nickname;

    private String email;

    private Integer sex;

    private Integer deptId;

    private Boolean enabled;

    @TableField(exist = false)
    private Integer[] roles;

    @TableField(exist = false)
    private String deptName;
}
