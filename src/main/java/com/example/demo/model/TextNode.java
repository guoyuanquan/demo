package com.example.demo.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：guoyq
 * @name：TextNode
 * @Date：2025/6/20 17:59
 * @Filename：TextNode
 */
public class TextNode {
    public String content;      // 节点内容（父节点可能为null）
    public List<TextNode> children; // 子节点列表

    public TextNode(String content, List<TextNode> children) {
        this.content = content;
        this.children = children;
    }

    /**
     * 判断是否是叶子节点
     */
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    /**
     * 获取完整内容（递归拼接）
     */
    public String getFullContent() {
        if (isLeaf()) {
            return content != null ? content : "";
        }

        StringBuilder sb = new StringBuilder();
        for (TextNode child : children) {
            sb.append(child.getFullContent()).append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * 获取节点元数据
     */
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("isLeaf", isLeaf());
        meta.put("contentLength", isLeaf() ? content.length() : getFullContent().length());
        meta.put("childCount", isLeaf() ? 0 : children.size());
        return meta;
    }

    @Override
    public String toString() {
        if (isLeaf()) {
            return String.format("叶子节点: %d字符", content.length());
        }
        return String.format("父节点: %d个子节点", children.size());
    }
}
