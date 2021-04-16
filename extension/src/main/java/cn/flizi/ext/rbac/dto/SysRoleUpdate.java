package cn.flizi.ext.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel
public class SysRoleUpdate {
    @ApiModelProperty(value = "ID", required = true, example = "1")
    @NotEmpty
    private Integer id;
    @ApiModelProperty(value = "角色备注", required = true, example = "管理员")
    @NotEmpty
    private String remark;
}