package cn.flizi.ext.rbac.service;

import cn.flizi.ext.rbac.entity.SysRoleMenu;
import cn.flizi.ext.rbac.mapper.SysRoleMenuMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements IService<SysRoleMenu> {

    /**
     * 全更新, 会先删除说有绑定关系,再重新添加
     *
     * @param roleId 角色id
     * @param menus  菜单ids
     */
    public Boolean updateRoleMenu(Integer roleId, Integer[] menus) {
        List<SysRoleMenu> roleMenuList = Arrays.stream(menus).map(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            return roleMenu;
        }).collect(Collectors.toList());

        remove(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId));
        saveBatch(roleMenuList);
        return true;
    }
}