package cn.flizi.ext.rbac.service;

import cn.flizi.ext.rbac.entity.SysRole;
import cn.flizi.ext.rbac.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> implements IService<SysRole> {

    @Override
    public boolean save(SysRole entity) {
        int count = count(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getName, entity.getName()));
        Assert.isTrue(count > 0, "角色标识重复");
        return super.save(entity);
    }
}
