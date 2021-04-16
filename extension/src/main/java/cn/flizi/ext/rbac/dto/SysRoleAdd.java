package cn.flizi.ext.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel
public class SysRoleAdd {
    @ApiModelProperty(value = "角色标识", required = true, example = "USER_ADMIN")
    @NotEmpty
    private String name;
    @ApiModelProperty(value = "角色备注", required = true, example = "管理员")
    @NotEmpty
    private String remark;
}