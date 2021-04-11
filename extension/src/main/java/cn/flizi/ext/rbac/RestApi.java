package cn.flizi.ext.rbac;

import cn.flizi.core.annition.Log;
import cn.flizi.core.util.R;
import cn.flizi.ext.rbac.entity.SysRole;
import cn.flizi.ext.rbac.service.SysRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Api(tags = "扩展接口,不需要在话请 pom.xml 中移除依赖 extension 依赖")
public class RestApi {

    private final SysRoleService sysRoleService;

    @Log("角色创建")
    @ApiOperation("角色创建")
    @PreAuthorize("hasAuthority('sys:role:add')")
    @PostMapping("/sys/role")
    public R create(@RequestBody SysRole sysRole) {
        return R.ok(sysRoleService.saveOrUpdate(sysRole));
    }

    @Log("角色删除")
    @ApiOperation("角色删除")
    @PreAuthorize("hasAuthority('sys:role:del')")
    @DeleteMapping("/sys/role")
    public R delete(@RequestParam List<Integer> ids) {
        return R.ok(sysRoleService.removeByIds(ids));
    }

    @Log("角色更新")
    @ApiOperation("角色更新")
    @PreAuthorize("hasAuthority('sys:role:update')")
    @PutMapping("/sys/role")
    public R update(@RequestBody SysRole sysRole) {
        return R.ok(sysRoleService.updateById(sysRole));
    }

    @Log(value = "角色分页")
    @ApiOperation("角色分页")
    @PreAuthorize("hasAuthority('sys:role:page')")
    @GetMapping("/sys/role/page")
    public R page(Page<SysRole> page) {
        return R.ok(sysRoleService.page(page));
    }
}
