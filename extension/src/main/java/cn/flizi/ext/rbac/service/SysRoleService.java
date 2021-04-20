package cn.flizi.ext.rbac.service;

import cn.flizi.ext.rbac.dto.SysRoleAdd;
import cn.flizi.ext.rbac.dto.SysRoleUpdate;
import cn.flizi.ext.rbac.entity.SysRole;
import cn.flizi.ext.rbac.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> implements IService<SysRole> {

    private final SysRoleMenuService sysRoleMenuService;

    /**
     * 创建角色
     *
     * @param body
     * @return
     */
    public Boolean saveRole(SysRoleAdd body) {
        // 检查
        int count = count(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getName, body.getName()));
        Assert.isTrue(count > 0, "角色标识重复");

        // 创建
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(body, sysRole);
        sysRole.insert();

        // 更新关联菜单
        return sysRoleMenuService.updateRoleMenu(sysRole.getId(), body.getMenus());
    }


    /**
     * 更新角色
     *
     * @param body
     * @return
     */
    public Boolean updateRole(SysRoleUpdate body) {
        // 检查
        Integer roleId = body.getId();
        SysRole role = getById(roleId);
        Assert.notNull(role, "确实不存在");
        Assert.isTrue(!role.getIsLock(), "当前角色状态被锁定,禁止修改");

        // 更新关联菜单
        return sysRoleMenuService.updateRoleMenu(roleId, body.getMenus());
    }
}
