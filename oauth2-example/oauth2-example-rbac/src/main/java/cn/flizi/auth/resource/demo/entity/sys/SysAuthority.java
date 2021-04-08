package cn.flizi.auth.resource.demo.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@TableName("sys_authority")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysAuthority extends Model<SysAuthority> {

    public static final int MENU = 0;
    public static final int FUNCTION = 1;

    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)

    @ApiModelProperty(value = "ID")
    private Integer id;


    @ApiModelProperty(value = "权限标识")
    private String authority;


    @ApiModelProperty(value = "名称")
    private String name;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "HTTP 路径")
    private String path;

    @ApiModelProperty(value = "HTTP 方法")
    private String method;
}
