package com.example.demo.controller.sperate;

/**
 * @Author：guoyq
 * @name：ParentChildSplitter
 * @Date：2025/7/3 14:58
 * @Describetion:
 */
import java.util.*;
import java.util.regex.*;

public class UniversalDocumentSplitter {

    // 配置参数
    private final int maxSegmentSize;
    private final int minSegmentSize;
    private final int overlapSize;
    private final boolean preserveStructure;

    public UniversalDocumentSplitter(int maxSegmentSize, int minSegmentSize, int overlapSize, boolean preserveStructure) {
        this.maxSegmentSize = maxSegmentSize;
        this.minSegmentSize = minSegmentSize;
        this.overlapSize = overlapSize;
        this.preserveStructure = preserveStructure;

        // 参数验证
        if (maxSegmentSize <= minSegmentSize) {
            throw new IllegalArgumentException("最大分段大小必须大于最小分段大小");
        }
        if (overlapSize < 0) {
            throw new IllegalArgumentException("重叠大小不能为负数");
        }
    }

    /**
     * 主入口：处理文档并返回分段结果
     *
     * @param content 输入的文档内容
     * @return 分段列表
     */
    public List<DocumentSegment> splitDocument(String content) {
        // 预处理：规范化空白字符
        String normalizedContent = normalizeWhitespace(content);

        // 识别文档结构
        List<StructuralElement> structure = identifyStructure(normalizedContent);

        // 生成分段
        List<DocumentSegment> segments = generateSegments(normalizedContent, structure);

        return segments;
    }

    /**
     * 规范化空白字符
     */
    private String normalizeWhitespace(String content) {
        // 替换连续空白字符为单个空格
        return content.replaceAll("\\s+", " ").trim();
    }

    /**
     * 识别文档结构
     */
    private List<StructuralElement> identifyStructure(String content) {
        List<StructuralElement> elements = new ArrayList<>();

        // 1. 尝试识别标题结构（Markdown风格）
        List<Heading> headings = detectHeadings(content);
        if (!headings.isEmpty() && preserveStructure) {
            elements.addAll(headings);
            return elements;
        }

        // 2. 识别段落结构
        List<Paragraph> paragraphs = detectParagraphs(content);
        if (!paragraphs.isEmpty()) {
            elements.addAll(paragraphs);
            return elements;
        }

        // 3. 作为连续文本处理
        elements.add(new TextBlock(0, content.length()));
        return elements;
    }

    /**
     * 检测标题结构（Markdown风格）
     */
    private List<Heading> detectHeadings(String content) {
        List<Heading> headings = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?m)^(#{1,6})\\s+(.+)$");
        Matcher matcher = pattern.matcher(content);

        int lastEnd = 0;
        Heading lastHeading = null;

        while (matcher.find()) {
            int level = matcher.group(1).length();
            String title = matcher.group(2);
            int start = matcher.start();
            int end = matcher.end();

            // 添加标题之间的内容块
            if (start > lastEnd) {
                String textContent = content.substring(lastEnd, start).trim();
                if (!textContent.isEmpty()) {
                    headings.add(new Heading(level,title,lastEnd, start));
                }
            }

            // 创建新标题
            Heading heading = new Heading(level, title, start, end);
            headings.add(heading);
            lastHeading = heading;
            lastEnd = end;
        }

        // 添加末尾内容
        if (lastEnd < content.length()) {
            String textContent = content.substring(lastEnd).trim();
            if (!textContent.isEmpty()) {
//                headings.add(new TextBlock(lastEnd, content.length()));
            }
        }

        return headings;
    }

    /**
     * 检测段落结构
     */
    private List<Paragraph> detectParagraphs(String content) {
        List<Paragraph> paragraphs = new ArrayList<>();
        // 段落分隔符：两个或多个换行符
        String[] parts = content.split("\\n{2,}");

        int currentPos = 0;
        for (String part : parts) {
            if (part.trim().isEmpty()) continue;

            int start = content.indexOf(part, currentPos);
            int end = start + part.length();
            paragraphs.add(new Paragraph(start, end));
            currentPos = end;
        }

        return paragraphs;
    }

    /**
     * 生成分段
     */
    private List<DocumentSegment> generateSegments(String content, List<StructuralElement> structure) {
        List<DocumentSegment> segments = new ArrayList<>();
        int segmentId = 1;

        for (StructuralElement element : structure) {
            String elementContent = content.substring(element.start, element.end);

            // 处理不同类型的结构元素
            if (element instanceof Heading) {
                segments.add(new DocumentSegment(
                        segmentId++,
                        "heading:" + ((Heading) element).level,
                        elementContent,
                        element.start
                ));
            }
            else if (element instanceof TextBlock || element instanceof Paragraph) {
                // 拆分大文本块
                if (elementContent.length() > maxSegmentSize) {
                    segments.addAll(splitLargeText(elementContent, element.start, segmentId));
                    segmentId += countSegmentsNeeded(elementContent.length(), maxSegmentSize);
                } else {
                    segments.add(new DocumentSegment(
                            segmentId++,
                            element instanceof Paragraph ? "paragraph" : "text",
                            elementContent,
                            element.start
                    ));
                }
            }
        }

        // 应用重叠（如果需要）
        if (overlapSize > 0) {
            applyOverlap(segments, content);
        }

        return segments;
    }

    /**
     * 拆分大文本块
     */
    private List<DocumentSegment> splitLargeText(String content, int globalOffset, int startId) {
        List<DocumentSegment> segments = new ArrayList<>();
        int segmentId = startId;
        int start = 0;

        while (start < content.length()) {
            int end = findOptimalSplitPoint(content, start);
            String segmentContent = content.substring(start, end);

            segments.add(new DocumentSegment(
                    segmentId++,
                    "text-segment",
                    segmentContent,
                    globalOffset + start
            ));

            // 更新位置（应用重叠）
            start = Math.max(start, end - overlapSize);
        }

        return segments;
    }

    /**
     * 查找最佳分割点
     */
    private int findOptimalSplitPoint(String content, int start) {
        int proposedEnd = Math.min(start + maxSegmentSize, content.length());
        if (proposedEnd == content.length()) return proposedEnd;

        // 1. 尝试在句子边界分割
        int sentenceEnd = findSentenceEnd(content, start, proposedEnd);
        if (sentenceEnd != -1) return sentenceEnd;

        // 2. 尝试在标点符号后分割
        int punctuationEnd = findPunctuation(content, start, proposedEnd);
        if (punctuationEnd != -1) return punctuationEnd;

        // 3. 尝试在单词边界分割
        int wordEnd = findWordBoundary(content, start, proposedEnd);
        if (wordEnd != -1 && (wordEnd - start) >= minSegmentSize) return wordEnd;

        // 4. 使用硬分割
        return proposedEnd;
    }

    /**
     * 查找句子结束位置
     */
    private int findSentenceEnd(String content, int start, int end) {
        // 从后往前查找句子结束符
        for (int i = end - 1; i > start + minSegmentSize; i--) {
            char c = content.charAt(i);
            if (c == '.' || c == '!' || c == '?') {
                // 确保不是缩写中的点
                if (i + 1 < content.length() && Character.isWhitespace(content.charAt(i + 1))) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * 查找标点符号位置
     */
    private int findPunctuation(String content, int start, int end) {
        // 查找常见的分割点
        for (int i = end - 1; i > start + minSegmentSize; i--) {
            char c = content.charAt(i);
            if (c == ',' || c == ';' || c == ':' || c == '—' || c == '-') {
                // 确保后面有空格
                if (i + 1 < content.length() && Character.isWhitespace(content.charAt(i + 1))) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * 查找单词边界
     */
    private int findWordBoundary(String content, int start, int end) {
        // 查找最后一个空格
        int lastSpace = content.lastIndexOf(' ', end);
        if (lastSpace > start + minSegmentSize) {
            return lastSpace + 1; // 包含空格
        }
        return -1;
    }

    /**
     * 应用重叠
     */
    private void applyOverlap(List<DocumentSegment> segments, String fullContent) {
        if (segments.size() < 2) return;

        for (int i = 1; i < segments.size(); i++) {
            DocumentSegment prev = segments.get(i - 1);
            DocumentSegment current = segments.get(i);

            // 跳过不同类型的分段
            if (!prev.type.equals(current.type)) continue;

            // 计算重叠内容
            int overlapStart = Math.max(prev.globalOffset,
                    current.globalOffset - overlapSize);
            String overlapContent = fullContent.substring(
                    overlapStart, current.globalOffset);

            // 更新当前分段内容
            current.content = overlapContent + current.content;
            current.globalOffset = overlapStart;
        }
    }

    /**
     * 计算需要的分段数量
     */
    private int countSegmentsNeeded(int contentLength, int maxSize) {
        int segments = contentLength / maxSize;
        if (contentLength % maxSize != 0) segments++;
        return segments;
    }

    // ================= 数据结构 =================

    /**
     * 文档分段
     */
    public static class DocumentSegment {
        public int id;
        public String type; // "heading", "paragraph", "text", "text-segment"
        public String content;
        public int globalOffset; // 在原始文档中的偏移量

        public DocumentSegment(int id, String type, String content, int globalOffset) {
            this.id = id;
            this.type = type;
            this.content = content;
            this.globalOffset = globalOffset;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s (%d chars @%d)",
                    id, type, content.length(), globalOffset);
        }

        public Map<String, Object> getMetadata() {
            Map<String, Object> meta = new HashMap<>();
            meta.put("id", id);
            meta.put("type", type);
            meta.put("length", content.length());
            meta.put("offset", globalOffset);
            return meta;
        }
    }

    /**
     * 结构元素基类
     */
    private static abstract class StructuralElement {
        int start;
        int end;

        public StructuralElement(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    /**
     * 标题元素
     */
    private static class Heading extends StructuralElement {
        int level;
        String title;

        public Heading(int level, String title, int start, int end) {
            super(start, end);
            this.level = level;
            this.title = title;
        }
    }

    /**
     * 段落元素
     */
    private static class Paragraph extends StructuralElement {
        public Paragraph(int start, int end) {
            super(start, end);
        }
    }

    /**
     * 文本块元素
     */
    private static class TextBlock extends StructuralElement {
        public TextBlock(int start, int end) {
            super(start, end);
        }
    }

    // ================= 使用示例 =================

    public static void main(String[] args) {
        // 示例1：结构化文档（Markdown）
        String markdownDoc = "# 文档标题\n\n" +
                "这是文档的简介部分，介绍文档的主要内容和目的。\n\n" +
                "## 第一部分\n\n" +
                "这是第一部分的详细内容，包含多个段落和说明。\n\n" +
                "### 子章节1.1\n\n" +
                "这是子章节的内容，通常更加具体和详细。";

        // 示例2：普通文本
        String plainText = "这是一段连续的文本内容，没有明显的标题或段落结构。"
                + "文本内容可能很长，需要被合理地分割成较小的片段，"
                + "以便后续处理或分析。分割时需要注意保持语义的完整性，"
                + "避免在单词或句子中间分割。";

        // 示例3：混合内容
        String mixedContent = "文档开头\n\n" +
                "# 主标题\n" +
                "这是主标题下的内容。\n\n" +
                "接下来是没有标题的连续文本："
                + "这段文本需要被自动分割，因为它没有明显的结构标记。"
                + "分割算法需要智能地处理这种情况。";

        // 创建拆分器
        UniversalDocumentSplitter splitter = new UniversalDocumentSplitter(
                200, // 最大分段大小（字符）
                50,  // 最小分段大小
                30,  // 重叠大小
                true // 保留结构
        );

        System.out.println("===== 结构化文档拆分 =====");
        processDocument(splitter, markdownDoc);

        System.out.println("\n===== 普通文本拆分 =====");
        processDocument(splitter, plainText);

        System.out.println("\n===== 混合内容拆分 =====");
        processDocument(splitter, mixedContent);
    }

    private static void processDocument(UniversalDocumentSplitter splitter, String content) {
        List<DocumentSegment> segments = splitter.splitDocument(content);

        System.out.println("原始文档长度: " + content.length());
        System.out.println("生成分段数量: " + segments.size());

        for (DocumentSegment segment : segments) {
            System.out.println(segment);
            String preview = segment.content.substring(0, Math.min(50, segment.content.length()));
            if (segment.content.length() > 50) preview += "...";
            System.out.println("内容预览: " + preview);
            System.out.println("----");
        }
    }
}
