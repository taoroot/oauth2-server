package cn.flizi.auth.resource.demo.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("sys_post")
@EqualsAndHashCode(callSuper = true)
public class SysPost extends Model<SysPost> {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String post;

    private Integer sort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
