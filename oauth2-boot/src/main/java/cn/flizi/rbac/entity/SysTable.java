package cn.flizi.rbac.entity;

import lombok.Data;

import java.util.List;

/**
 * @author zhiyi
 * @date 2020/11/3 22:27
 */
@Data
public class SysTable {
    //表的名称
    private String tableName;
    //表的备注
    private String comments;
    //表的主键
    private SysTableColumn pk;
    //表的列名(不包含主键)
    private List<SysTableColumn> columns;
    //类名(第一个字母大写)，如：sys_user => SysUser
    private String className;
    //类名(第一个字母下写)，如：sys_user => sysUser
    private String lowerClassName;

    @Data
    public static class SysTableColumn {
        //列名
        private String columnName;
        //列名类型
        private String dataType;
        //列名备注
        private String comments;

        //属性名称(第一个字母大写)，如：user_name => UserName
        private String attrName;
        //属性名称(第一个字母小写)，如：user_name => userName
        private String lowerAttrName;
        //属性类型
        private String attrType;
        //auto_increment
        private String extra;
    }
}
