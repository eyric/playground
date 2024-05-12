package org.example.common.util;

import java.util.*;
import java.util.stream.Collectors;

public class TreeUtil {

    public static TreeNode makeTree(List<TreeNode> list) {
        TreeNode root = TreeNode.builder().children(new ArrayList<>()).build();
        if (CollUtil.isEmpty(list)) {
            return root;
        }

        // 将数据根据id排序
        list = list.stream().sorted(Comparator.comparing(TreeNode::getId)).collect(Collectors.toList());

        // 构建树形结构
        Map<Long, TreeNode> nodeMap = new HashMap<>();
        for (TreeNode node : list) {
            node.setChildren(new ArrayList<>());
            nodeMap.put(node.getId(), node);
            if (node.getParentId() == null || node.getParentId() <= 0) {
                root.getChildren().add(node);
            } else {
                TreeNode parentNode = nodeMap.get(node.getParentId());
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                }
            }
        }

        return root;
    }
}
