package cn.flizi.ext.rbac.service;

import cn.flizi.core.util.R;
import cn.flizi.ext.rbac.entity.SysAuthority;
import cn.flizi.ext.rbac.mapper.SysAuthorityMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysAuthorityService extends ServiceImpl<SysAuthorityMapper, SysAuthority> implements IService<SysAuthority> {

    public R<IPage<SysAuthority>> pageByMenu(Integer menuId, Page<SysAuthority> page) {
        return null;
    }
}
