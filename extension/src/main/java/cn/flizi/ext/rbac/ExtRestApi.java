package cn.flizi.ext.rbac;

import cn.flizi.core.annition.Log;
import cn.flizi.core.util.R;
import cn.flizi.ext.rbac.dto.SysMenuAdd;
import cn.flizi.ext.rbac.dto.SysMenuUpdate;
import cn.flizi.ext.rbac.dto.SysRoleAdd;
import cn.flizi.ext.rbac.dto.SysRoleUpdate;
import cn.flizi.ext.rbac.entity.SysAuthority;
import cn.flizi.ext.rbac.entity.SysMenu;
import cn.flizi.ext.rbac.service.SysAuthorityService;
import cn.flizi.ext.rbac.service.SysMenuService;
import cn.flizi.ext.rbac.service.SysRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Api(tags = "扩展接口")
public class ExtRestApi {

    private final SysRoleService sysRoleService;
    private final SysAuthorityService sysAuthorityService;
    private final SysMenuService sysMenuService;


    @Log("角色创建")
    @ApiOperation(value = "角色创建", notes = "menus是当前角色的所有菜单id")
    @PreAuthorize("hasAuthority('sys:role:add')")
    @PostMapping("/sys/role")
    public R sysRoleAdd(@RequestBody @Valid SysRoleAdd body) {
        return R.ok(sysRoleService.saveRole(body));
    }

    @Log("角色删除")
    @ApiOperation("角色删除")
    @PreAuthorize("hasAuthority('sys:role:del')")
    @DeleteMapping("/sys/role")
    public R sysRoleDel(@RequestParam List<Integer> ids) {
        return R.ok(sysRoleService.removeByIds(ids));
    }

    @Log("角色更新")
    @ApiOperation(value = "角色更新", notes = "menus是当前角色的所有菜单id")
    @PreAuthorize("hasAuthority('sys:role:update')")
    @PutMapping("/sys/role")
    @ApiIgnore
    public R sysRoleUpdate(@RequestBody @Valid SysRoleUpdate body) {
        return R.ok(sysRoleService.updateRole(body));
    }

    @Log(value = "角色分页")
    @ApiOperation("角色分页")
    @PreAuthorize("hasAuthority('sys:role:page')")
    @GetMapping("/sys/role/page")
    @ApiParam("page")
    public R sysRolePage(@RequestParam Integer current, @RequestParam Integer size) {
        return R.ok(sysRoleService.page(new Page<>(current, size)));
    }

    @Log(value = "菜单树")
    @ApiOperation("菜单树")
    @PreAuthorize("hasAuthority('sys:menu:tree')")
    @GetMapping(value = "/sys/menu/tree")
    public R<List<?>> sysMenuTree() {
        return sysMenuService.getTree();
    }

    @Log("菜单详情")
    @ApiOperation("菜单详情")
    @PreAuthorize("hasAuthority('sys:menu:get')")
    @GetMapping("/sys/menu/{id}")
    public R<SysMenu> sysMenuGet(@PathVariable Integer id) {
        return R.ok(sysMenuService.getById(id));
    }


    @Log("菜单强删")
    @ApiOperation("菜单强删")
    @PreAuthorize("hasAuthority('sys:menu:delforce')")
    @DeleteMapping("/sys/menu/force")
    public R sysMenuDelForce(@RequestParam Integer id) {
        Assert.isTrue(id != -1, "不能删除根节点");
        return R.ok(sysMenuService.removeForce(id));
    }


    @Log("菜单删除")
    @ApiOperation("菜单删除")
    @PreAuthorize("hasAuthority('sys:menu:del')")
    @DeleteMapping("/sys/menu")
    public R sysMenuDel(@RequestParam List<Integer> ids) {
        return R.ok(sysMenuService.removeByIds(ids));
    }


    @Log("菜单创建")
    @ApiOperation("菜单创建")
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PostMapping("/sys/menu")
    public R sysMenuAdd(@RequestBody SysMenuAdd body) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(body, sysMenu);
        return R.ok(sysMenuService.save(sysMenu));
    }


    @Log("菜单更新")
    @ApiOperation("菜单更新")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @PutMapping("/sys/menu")
    public R sysMenuUpdate(@RequestBody SysMenuUpdate body) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(body, sysMenu);
        return R.ok(sysMenuService.updateById(sysMenu));
    }

    @Log("菜单排序")
    @ApiOperation("菜单排序")
    @PreAuthorize("hasAuthority('sys:menu:sort')")
    @PutMapping("/sys/menu/sort")
    public R sysMenuSort(Integer menuId, Integer index) {
        return R.ok(sysMenuService.sort(menuId, index));
    }


    @Log(value = "权限分页")
    @ApiOperation("权限分页")
    @PreAuthorize("hasAuthority('sys:authority:page')")
    @GetMapping("/sys/authority/page")
    public R sysAuthorityPage(@RequestParam Integer current, @RequestParam Integer size) {
        return R.ok(sysAuthorityService.page(new Page<>(current, size)));
    }


    @Log("菜单的权限")
    @ApiOperation("菜单的权限")
    @PreAuthorize("hasAuthority('sys:menu:authority:page')")
    @GetMapping("/sys/menu/authority/page")
    public R sysMenuAuthorityPage(Integer menuId, Page<SysAuthority> page) {
        return R.ok(sysAuthorityService.pageByMenu(menuId, page));
    }

    @Log("菜单的权限新增")
    @ApiOperation("菜单的权限新增")
    @PreAuthorize("hasAuthority('sys:menu:authority:add')")
    @PostMapping("/sys/menu/authority")
    public R sysMenuAuthority(@RequestParam("menuId") Integer menuId, @RequestBody List<Integer> ids) {
        return R.ok(sysMenuService.addAuthorityByMenu(menuId, ids));
    }

    @Log("菜单的权限移除")
    @ApiOperation("菜单的权限移除")
    @PreAuthorize("hasAuthority('sys:menu:authority:del')")
    @DeleteMapping("/sys/menu/authority")
    public R sysMenuAuthorityDel(@RequestParam("menuId") Integer menuId, @RequestBody List<Integer> ids) {
        return R.ok(sysMenuService.removeAuthorityByMenu(menuId, ids));
    }
}
