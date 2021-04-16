package cn.flizi.ext.rbac.service;

import cn.flizi.core.util.R;
import cn.flizi.core.util.TreeUtils;
import cn.flizi.ext.rbac.entity.SysMenu;
import cn.flizi.ext.rbac.mapper.SysMenuMapper;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
        List<SysMenu> menuList = baseMapper.selectList(Wrappers.emptyWrapper());
        List<Tree<Integer>> authorityTree = TreeUtil.build(menuList, -1, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getName());
            tree.putExtra("path", treeNode.getPath());
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("component", treeNode.getComponent());
            tree.putExtra("hidden", treeNode.getHidden());
            tree.putExtra("alwaysShow", treeNode.getAlwaysShow());
            tree.putExtra("redirect", treeNode.getRedirect());
            tree.putExtra("title", treeNode.getTitle());
            tree.putExtra("icon", treeNode.getIcon());
            tree.putExtra("authority", treeNode.getAuthority());
            tree.putExtra("breadcrumb", treeNode.getBreadcrumb());
        });
        if (authorityTree.size() == 0) {
            return R.ok(menuList);
        }
        // 排序
        TreeUtils.computeSort(authorityTree);
        // 计算路径
        computePath(authorityTree);
        return R.ok(authorityTree);
    }

    public static void computePath(List<? extends Tree<Integer>> trees) {
        if (trees == null || trees.size() == 0) {
            return;
        }
        for (Tree<Integer> parent : trees) {
            if (parent.getChildren() == null) {
                return;
            }
            parent.putExtra("absPath", parent.get("path"));
            for (Tree<Integer> child : parent.getChildren()) {
                String path1 = (String) parent.get("path");
                String path2 = (String) child.get("path");
                if (!StrUtil.startWithAny(path2, "http", "https")) {
                    child.putExtra("absPath", path1 + "/" + path2);
                } else {
                    child.putExtra("absPath", path2);
                }
            }
            computePath(parent.getChildren());
        }
    }

}