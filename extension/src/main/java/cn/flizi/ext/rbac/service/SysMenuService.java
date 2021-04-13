package cn.flizi.ext.rbac.service;

import cn.flizi.core.util.R;
import cn.flizi.ext.rbac.entity.SysMenu;
import cn.flizi.ext.rbac.mapper.SysMenuMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> implements IService<SysMenu> {

    public Object sort(Integer menuId, Integer index) {
        return null;
    }

    public Object removeForce(Integer id) {
        return null;
    }

    public Object addAuthorityByMenu(Integer menuId, List<Integer> ids) {
        return null;
    }

    public Object removeAuthorityByMenu(Integer menuId, List<Integer> ids) {
        return null;
    }

    public R<List<?>> getTree() {
        return null;
    }
}