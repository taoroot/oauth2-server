package cn.flizi.auth.resource.demo.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends Model<SysRole> {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private Integer scopeType;

    private Boolean isLock = false;

    private Integer deptId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.ARRAY)
    private Integer[] scope;

    @TableField(exist = false)
    private Integer[] menus;
}
