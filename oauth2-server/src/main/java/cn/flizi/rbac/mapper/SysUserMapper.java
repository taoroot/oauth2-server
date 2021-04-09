package cn.flizi.rbac.mapper;

import cn.flizi.rbac.entity.SysMenu;
import cn.flizi.rbac.entity.SysRole;
import cn.flizi.rbac.entity.SysUser;
import cn.flizi.rbac.entity.SysUserSocial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : zhiyi
 * Date: 2020/2/11
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<Integer> roleIds(@Param("userId") Integer userId);

    List<SysRole> roles(@Param("userId") Integer userId);


    List<SysMenu> menus(@Param("userId") Integer userId, @Param("type") Integer type);

    List<SysUserSocial> socials(@Param("userId") Integer userId);

    List<String> authorityNames(@Param("userId") Integer userId);

    IPage<SysUser> getPage(@Param("page") Page<SysUser> page,
                           @Param("username") String username,
                           @Param("phone") String phone,
                           @Param("deptId") Integer deptId,
                           @Param("enabled") Boolean enabled);
}
