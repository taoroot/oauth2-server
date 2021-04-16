package cn.flizi.ext.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel
public class SysMenuUpdate {
    @ApiModelProperty(value = "ID", required = true, example = "1")
    @NotEmpty
    private Integer id;

    private String path;

    private String component;

    private Boolean hidden;

    private Boolean alwaysShow;

    private String redirect;

    private String title;

    private String authority;

    private String icon;

    private Boolean breadcrumb;

    private Boolean noCache;

    private Integer parentId;

    private Integer weight;

    private Integer type;
}