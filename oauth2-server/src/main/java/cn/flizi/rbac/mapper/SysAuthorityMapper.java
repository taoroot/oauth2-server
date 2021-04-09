package cn.flizi.rbac.mapper;

import cn.flizi.rbac.entity.SysAuthority;
import cn.flizi.rbac.entity.SysMenuAuthority;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysAuthorityMapper extends BaseMapper<SysAuthority> {

    IPage<SysAuthority> selectByMenu(@Param("page") Page<SysAuthority> page,
                                     @Param(Constants.WRAPPER) LambdaQueryWrapper<SysMenuAuthority> wrapper);

}
