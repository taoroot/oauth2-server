package cn.flizi.rbac.mapper;

import cn.flizi.rbac.entity.SysLog;
import cn.flizi.rbac.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author : zhiyi
 * Date: 2020/2/11
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    IPage<SysLog> getPage(@Param("page") Page<SysLog> page,
                          @Param(Constants.WRAPPER) QueryWrapper<SysUser> wrapper);
}
