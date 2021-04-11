package cn.flizi.ext.rbac.service;

import cn.flizi.ext.rbac.entity.SysRole;
import cn.flizi.ext.rbac.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> implements IService<SysRole> {

}
