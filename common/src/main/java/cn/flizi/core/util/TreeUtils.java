package cn.flizi.core.util;

import cn.hutool.core.lang.tree.Tree;

import java.util.List;

public class TreeUtils {
    public static final int ROOT_PARENT_ID = -1;

    public static void computeSort(List<? extends Tree<?>> trees) {
        if (trees == null || trees.size() == 0) {
            return;
        }
        for (int i = 0; i < trees.size(); i++) {
            try {
                trees.get(i).putExtra("sortPrev", trees.get(i - 1).getId());
            } catch (Exception e) {
                trees.get(i).putExtra("sortPrev", null);
            }
            try {
                trees.get(i).putExtra("sortNext", trees.get(i + 1).getId());
            } catch (Exception e) {
                trees.get(i).putExtra("sortNext", null);
            }
            computeSort(trees.get(i).getChildren());
        }
    }
}