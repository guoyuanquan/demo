package com.example.demo.controller.sperate;


import com.example.demo.model.TextNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlainTextHierarchySplitter  {

    // 配置参数
    private final int maxParentSize;
    private final int maxChildSize;
    private final int overlapSize;

    public PlainTextHierarchySplitter(int maxParentSize, int maxChildSize , int overlapSize) {
        this.maxParentSize = maxParentSize;
        this.maxChildSize = maxChildSize;
        this.overlapSize = overlapSize;

        // 参数验证
        if (maxParentSize <= maxChildSize) {
            throw new IllegalArgumentException("父段大小必须大于子段大小");
        }
        if (maxChildSize <= 0 || overlapSize < 0) {
            throw new IllegalArgumentException("参数值无效");
        }
    }

    /**
     * 主入口：处理文本并返回父子分段结构
     *
     * @param content 输入的文本内容
     * @return 父子分段列表（顶级父段）
     */
    public List<TextNode> split(String content) {
        // 预处理：规范化空白字符
        String normalized = normalizeWhitespace(content);

        // 分割段落
        List<String> paragraphs = splitParagraphs(normalized);

        // 构建父子结构
        List<TextNode> parentNodes = new ArrayList<>();
        for (String para : paragraphs) {
            if (para.isEmpty()) continue;

            if (para.length() <= maxParentSize) {
                // 创建叶子节点（父段）
                parentNodes.add(new TextNode(para, null));
            } else {
                // 创建父节点并添加子节点
                TextNode parent = new TextNode(para, new ArrayList<>());
                processParagraph(para, parent);
                parentNodes.add(parent);
            }
        }

        return parentNodes;
    }

    /**
     * 规范化空白字符
     */
    private String normalizeWhitespace(String content) {
        // 替换连续空白字符为单个空格
        return content.replaceAll("\\s+", " ").trim();
    }

    /**
     * 分割段落
     */
    private List<String> splitParagraphs(String content) {
        // 按两个或多个换行符分割
        return Arrays.asList(content.split("\\n{2,}"));
    }

    /**
     * 处理段落文本
     */
    private void processParagraph(String paragraph, TextNode parent) {
        // 先尝试按句子分割
        List<String> sentences = splitSentences(paragraph);

        for (String sentence : sentences) {
            if (sentence.isEmpty()) continue;

            if (sentence.length() <= maxChildSize) {
                // 添加子节点
                parent.children.add(new TextNode(sentence, null));
            } else {
                // 长句子需要进一步拆分
                List<TextNode> chunks = splitLongSentence(sentence);
                parent.children.addAll(chunks);
            }
        }
    }

    /**
     * 分割句子
     */
    private List<String> splitSentences(String text) {
        List<String> sentences = new ArrayList<>();
        // 使用正则表达式匹配句子边界
        Matcher matcher = Pattern.compile("[^.?!。？！]+([.?!。？！]|$)").matcher(text);

        while (matcher.find()) {
            String sentence = matcher.group().trim();
            if (!sentence.isEmpty()) {
                sentences.add(sentence);
            }
        }

        // 处理最后一句（如果没有结束标点）
        if (sentences.isEmpty() || !text.endsWith(sentences.get(sentences.size() - 1))) {
            String lastPart = text.substring(sentences.stream().mapToInt(String::length).sum());
            if (!lastPart.trim().isEmpty()) {
                sentences.add(lastPart.trim());
            }
        }

        return sentences;
    }

    /**
     * 分割长句子
     */
    private List<TextNode> splitLongSentence(String sentence) {
        List<TextNode> chunks = new ArrayList<>();
        int start = 0;

        while (start < sentence.length()) {
            int end = findOptimalSplitPoint(sentence, start);
            String chunkContent = sentence.substring(start, end).trim();

            if (!chunkContent.isEmpty()) {
                chunks.add(new TextNode(chunkContent, null));
            }

            // 更新起始位置（应用重叠）
            start = Math.max(start, end - overlapSize);
        }

        return chunks;
    }

    /**
     * 寻找最佳分割点
     */
    private int findOptimalSplitPoint(String text, int start) {
        int proposedEnd = Math.min(start + maxChildSize, text.length());
        if (proposedEnd == text.length()) return proposedEnd;

        // 1. 优先在句子标点后分割
        int punctuationPos = findPunctuation(text, start, proposedEnd);
        if (punctuationPos != -1) return punctuationPos;

        // 2. 在逗号、分号后分割
        int commaPos = findCommaOrSemicolon(text, start, proposedEnd);
        if (commaPos != -1) return commaPos;

        // 3. 在连字符后分割
        int hyphenPos = findHyphen(text, start, proposedEnd);
        if (hyphenPos != -1) return hyphenPos;

        // 4. 在单词边界分割
        int wordBoundary = findWordBoundary(text, start, proposedEnd);
        if (wordBoundary != -1) return wordBoundary;

        // 5. 最后使用硬分割
        return proposedEnd;
    }

    /**
     * 查找标点符号分割点
     */
    private int findPunctuation(String text, int start, int end) {
        for (int i = end - 1; i > start + maxChildSize; i--) {
            char c = text.charAt(i);
            if (c == '.' || c == '?' || c == '!' || c == '。' || c == '？' || c == '！') {
                // 确保不是缩写中的点
                if (i + 1 < text.length() && Character.isWhitespace(text.charAt(i + 1))) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * 查找逗号或分号分割点
     */
    private int findCommaOrSemicolon(String text, int start, int end) {
        for (int i = end - 1; i > start + maxChildSize; i--) {
            char c = text.charAt(i);
            if (c == ',' || c == ';' || c == '，' || c == '；') {
                if (i + 1 < text.length() && Character.isWhitespace(text.charAt(i + 1))) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * 查找连字符分割点
     */
    private int findHyphen(String text, int start, int end) {
        for (int i = end - 1; i > start + maxChildSize; i--) {
            char c = text.charAt(i);
            if (c == '-' || c == '—' || c == '–') {
                if (i + 1 < text.length() && Character.isWhitespace(text.charAt(i + 1))) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * 查找单词边界分割点
     */
    private int findWordBoundary(String text, int start, int end) {
        // 从后往前找空格
        int lastSpace = text.lastIndexOf(' ', end);
        if (lastSpace > start + maxChildSize) {
            return lastSpace + 1; // 包含空格
        }
        return -1;
    }


    // ================= 使用示例 =================

    public static void main(String[] args) {
        // 示例文本
        String sampleText = "在自然语言处理领域，文本分割是一个基础且重要的任务。"
                + "它涉及将连续文本划分为有意义的单元，如段落、句子或短语。"
                + "有效的文本分割能显著提升下游任务如机器翻译、信息检索和文本摘要的性能。\\n\\n"
                + "然而，处理普通文本时面临独特挑战：缺乏明确的结构标记，"
                + "句子长度变化大，以及需要保留上下文连贯性。"
                + "本算法通过父子分段结构解决这些问题，在保持语义完整性的同时提供灵活的分割方案。";

        // 创建拆分器
        PlainTextHierarchySplitter splitter = new PlainTextHierarchySplitter(
                150, // 父段最大大小
                50,  // 子段最大大小
                5   // 重叠大小
        );

        // 执行拆分
        List<TextNode> nodes = splitter.split(sampleText);

        // 输出结果
        System.out.println("=== 普通文本父子分段结果 ===");
        System.out.println("段落数量: " + nodes.size());

        for (int i = 0; i < nodes.size(); i++) {
            TextNode node = nodes.get(i);
            System.out.printf("\n段落 %d: %s%n", i + 1, node);

            if (!node.isLeaf()) {
                System.out.println("子节点数量: " + node.children.size());
                for (int j = 0; j < node.children.size(); j++) {
                    TextNode child = node.children.get(j);
                    System.out.printf("  子节点 %d: %s%n", j + 1, child.content);
                }
            } else {
                String preview = node.content.substring(0,
                        Math.min(60, node.content.length()));
                if (node.content.length() > 60) preview += "...";
                System.out.println("内容预览: " + node.content);
            }
        }
    }
}