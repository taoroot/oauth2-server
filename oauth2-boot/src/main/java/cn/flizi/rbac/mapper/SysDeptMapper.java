package cn.flizi.rbac.mapper;

import cn.flizi.rbac.entity.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : zhiyi
 * Date: 2020/2/11
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    List<SysDept> selectAll();
}
