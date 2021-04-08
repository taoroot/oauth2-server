package cn.flizi.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysCodeMapper {

    /**
     * 查询表列表
     *
     * @return
     */
    List<Map<String, String>> queryTableList();

    /**
     * 查询表信息
     *
     * @param tableName 表名称
     * @return
     */
    Map<String, String> queryTable(String tableName);

    /**
     * 查询表列信息
     *
     * @param tableName 表名称
     * @return
     */
    List<Map<String, String>> queryColumns(String tableName);
}
