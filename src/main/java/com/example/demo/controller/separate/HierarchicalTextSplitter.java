package com.example.demo.controller.separate;

/**
 * @Author：guoyq
 * @name：test
 * @Date：2025/6/6 15:02
 * @Describtion:文件拆解
 */

import com.example.demo.model.TextNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class HierarchicalTextSplitter  {
    // 配置参数
    private final int chunkSize;          // 目标块大小（字符数）
    private final int chunkOverlap;       // 重叠区大小（字符数）
    private final List<String> separators; // 分层分隔符（从粗到细）
    private final boolean keepSeparator;  // 是否保留分隔符

    public HierarchicalTextSplitter(int chunkSize, int chunkOverlap,
                                    List<String> separators, boolean keepSeparator) {
        if (chunkOverlap >= chunkSize) {
            throw new IllegalArgumentException("重叠大小必须小于块大小");
        }
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.separators = new ArrayList<>(separators);
        this.keepSeparator = keepSeparator;
    }

    // 默认分隔符配置（Markdown/文本适用）
    public static List<String> defaultSeparators() {
        return Arrays.asList(
                "\n##\\s+",  // 二级标题 (Markdown)
                "\n###\\s+", // 三级标题
                "\n\\*\\*\\s+\\*\\*", // 加粗文本
                "\n\\*\\s+",  // 列表项
                "\n",        // 换行
                "\\.\\s+",    // 句子
                "\\s+"        // 单词
        );
    }

    // 公开API：输入原始文本，返回拆分后的块列表
    public List<TextNode> split(String text) {
        // 先进行分层递归拆分
        List<String> chunks = recursiveSplit(text, separators);
        // 添加重叠区
        return addOverlaps(chunks);
    }

    // 核心递归拆分逻辑
    private List<String> recursiveSplit(String text, List<String> currentSeparators) {
        List<String> resultChunks = new ArrayList<>();

        // 终止条件1：文本已足够小
        if (text.length() <= chunkSize) {
            resultChunks.add(text);
            return resultChunks;
        }

        // 终止条件2：无更多分隔符可用（按字符拆分）
        if (currentSeparators.isEmpty()) {
            return splitByCharacter(text);
        }

        // 获取当前层级分隔符
        String sep = currentSeparators.get(0);
        List<String> nextSeparators = currentSeparators.subList(1, currentSeparators.size());

        // 使用当前分隔符拆分文本
        String[] sections = text.split("(?=" + Pattern.quote(sep) + ")", -1);
        List<String> validSections = new ArrayList<>();

        // 重组保留分隔符的片段
        for (int i = 0; i < sections.length; i++) {
            if (i > 0 && keepSeparator) {
                validSections.add(sep + sections[i]);
            } else {
                validSections.add(sections[i]);
            }
        }

        // 处理每个分段
        for (String section : validSections) {
            if (section.isEmpty()) {
                continue;
            }

            if (section.length() <= chunkSize) {
                // 满足大小要求，直接保留
                resultChunks.add(section);
            } else if (!nextSeparators.isEmpty()) {
                // 递归拆分大段落
                resultChunks.addAll(recursiveSplit(section, nextSeparators));
            } else {
                // 最终层处理：按字符拆分
                resultChunks.addAll(splitByCharacter(section));
            }
        }

        return resultChunks;
    }

    // 字符级拆分（最终层）
    private List<String> splitByCharacter(String text) {
        List<String> chunks = new ArrayList<>();
        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));
            start = end;
        }
        return chunks;
    }

    // 添加重叠区处理
    private List<TextNode> addOverlaps(List<String> chunks) {
        List<TextNode> nodeList = new ArrayList<>();
        if (chunks.size() <= 1 || chunkOverlap == 0) {
            for (String paragraph : chunks) {
                nodeList.add(new TextNode(paragraph,null)); // 逐个创建并添加到列表
            }
            return nodeList;
        }

        List<String> overlappedChunks = new ArrayList<>();
        overlappedChunks.add(chunks.get(0));  // 第一个块不变

        // 从第二个块开始处理重叠
        for (int i = 1; i < chunks.size(); i++) {
            String prevChunk = chunks.get(i - 1);
            String currentChunk = chunks.get(i);

            // 计算前一块的重叠部分
            int overlapStart = Math.max(0, prevChunk.length() - chunkOverlap);
            String overlapSection = prevChunk.substring(overlapStart);

            // 合并重叠部分到当前块
            StringBuilder newChunk = new StringBuilder();
            newChunk.append(overlapSection)
                    .append(currentChunk);

            // 处理块大小超限（递归拆分）
            if (newChunk.length() > chunkSize) {
                int splitPoint = findSafeSplitPoint(newChunk.toString());
                overlappedChunks.add(newChunk.substring(0, splitPoint));

                // 处理剩余部分
                String remaining = newChunk.substring(splitPoint);
                if (!remaining.isEmpty()) {
                    if (remaining.length() <= chunkSize) {
                        overlappedChunks.add(remaining);
                    } else {
                        overlappedChunks.addAll(splitByCharacter(remaining));
                    }
                }
            } else {
                overlappedChunks.add(newChunk.toString());
            }
        }

        for (String paragraph : overlappedChunks) {
            nodeList.add(new TextNode(paragraph,null)); // 逐个创建并添加到列表
        }

        return nodeList;
    }

    // 在语义边界处查找安全分割点
    private int findSafeSplitPoint(String text) {
        // 优先在句子/段落边界分割
        int[] candidatePoints = {
                text.lastIndexOf("\n\n", chunkSize),
                text.lastIndexOf("\n", chunkSize),
                text.lastIndexOf(". ", chunkSize),
                text.lastIndexOf("! ", chunkSize),
                text.lastIndexOf("? ", chunkSize),
                text.lastIndexOf(" ", chunkSize)
        };

        for (int point : candidatePoints) {
            if (point > chunkSize * 0.7) {  // 避免过早分割
                return point + 1;  // 包含边界字符
            }
        }

        // 无合适边界则硬分割
        return Math.min(chunkSize, text.length());
    }

    // 示例用法
    public static void main(String[] args) {
        String sampleText = "## 第1章\n这是第一章的导论内容...\n\n"
                + "## 第2章\n第二章包含多个主题:\n"
                + "### 主题A\n详细描述A...\n"
                + "### 主题B\n详细描述B...";

        // 创建拆分器 (块大小=100, 重叠=20)
        HierarchicalTextSplitter splitter = new HierarchicalTextSplitter(
                100, 20, defaultSeparators(), true
        );

        List<TextNode> chunks = splitter.split(sampleText);

        // 输出结果
        System.out.println("生成块数: " + chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println(i+1);
            System.out.println(chunks.get(i).content.length());
            System.out.printf( chunks.get(i).content);
        }

    }
}
