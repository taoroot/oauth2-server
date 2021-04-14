package cn.flizi.ext.rbac.mapper;

import cn.flizi.ext.rbac.entity.SysAuthority;
import cn.flizi.ext.rbac.entity.SysMenuAuthority;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysAuthorityMapper extends BaseMapper<SysAuthority> {

    @Select("select a.authority, a.name, a.path, a.method, ma.id from sys_menu_authority ma " +
            "left join sys_authority a on a.id = ma.authority_id ${ew.customSqlSegment}")
    IPage<SysAuthority> selectByMenu(@Param("page") Page<SysAuthority> page,
                                     @Param(Constants.WRAPPER) LambdaQueryWrapper<SysMenuAuthority> wrapper);
}