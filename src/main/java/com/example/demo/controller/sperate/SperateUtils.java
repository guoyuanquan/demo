package com.example.demo.controller.sperate;

/**
 * @Author：guoyq
 * @name：SperateUtils
 * @Date：2025/7/3 10:16
 * @Describetion:
 */
public class SperateUtils {

    // 规范化空白字符
    public String normalizeWhitespace(String text) {
        // 替换不间断空格
        text = text.replaceAll("\\u00A0", " ");

        // 统一换行符
        text = text.replaceAll("\\r\\n?", "\n");

        // 压缩连续换行符（保留段落结构）
        text = text.replaceAll("(\\n\\s*){3,}", "\n\n");

        // 压缩连续空格
        return text.replaceAll("\\s+", " ");
    }

    // 规范化标点符号
    public String normalizePunctuation(String text) {
        // 统一破折号
        text = text.replaceAll("[–—]", "-");

        // 处理全角标点
        text = text.replaceAll("，", ",")
                .replaceAll("。", ".")
                .replaceAll("！", "!")
                .replaceAll("？", "?")
                .replaceAll("；", ";")
                .replaceAll("：", ":")
                .replaceAll("……", "...");

        // 移除多余的重复标点
        text = text.replaceAll("([.!?])\\1{2,}", "$1$1"); // 保留最多两个重复标点

        return text;
    }

    // 处理特殊符号
    public String cleanSpecialCharacters(String text, boolean removeParentheses) {
        // 统一引号
        text = text.replaceAll("[“”]", "\"")
                .replaceAll("[‘’]", "'");

        // 可选：移除括号内容
        if (removeParentheses) {
            text = text.replaceAll("\\s*[\\(\\[\\{<].*?[\\)\\]\\}>]\\s*", " ");
        }

        // 移除孤立的特殊符号
        text = text.replaceAll("(\\s)[*@#&](\\s)", "$1$2");

        return text;
    }

    // 移除不可见字符
    public String removeInvisibleChars(String text) {
        // 移除控制字符（保留制表符和换行符）
        text = text.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");

        // 移除特定Unicode字符
        text = text.replaceAll("[\\u200B\\uFEFF\\u00AD]", "");

        return text;
    }
}
