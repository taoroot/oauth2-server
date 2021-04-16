package cn.flizi.ext.rbac.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class SysMenuAdd {
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