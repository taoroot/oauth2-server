package cn.flizi.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "sys_region")
public class SysRegion extends Model<SysRegion> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer type;

    private String code;

    private Integer parentId;
}
