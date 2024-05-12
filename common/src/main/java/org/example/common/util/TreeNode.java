package org.example.common.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreeNode {
    /**
     * 节点ID
     */
    private Long id;
    /**
     * 父节点ID
     */
    private Long parentId;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 子节点列表
     */
    private List<TreeNode> children;
}
