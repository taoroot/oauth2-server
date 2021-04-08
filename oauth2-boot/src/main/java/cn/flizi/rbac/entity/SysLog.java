package cn.flizi.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "sys_log")
public class SysLog extends Model<SysLog> {
    /**
     * 日志主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 操作模块
     */
    private String title;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作类别
     */
    private Integer operatorType;

    /**
     * 操作人员
     */
    private Integer userId;
    /**
     * 操作人员
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 部门
     */
    private Integer deptId;
    /**
     * 部门
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 请求url
     */
    private String url;

    /**
     * 操作地址
     */
    private String ip;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 返回参数
     */
    private String result;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String error;

    /**
     * 操作时间
     */
    private Long time;
}
