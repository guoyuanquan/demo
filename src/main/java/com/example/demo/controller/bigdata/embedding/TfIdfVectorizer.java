package com.example.demo.controller.bigdata.embedding;

/**
 * @Author：guoyq
 * @name：TfIdfEmbedding
 * @Date：2025/7/4 15:14
 * @Describetion:
 */
import java.util.*;

public class TfIdfVectorizer {
    private final Map<String, Integer> docFreq = new HashMap<>();
    private final List<Map<String, Integer>> docTermCounts = new ArrayList<>();
    private int totalDocs = 0;

    public void fit(List<List<String>> documents) {
        totalDocs = documents.size();
        for (List<String> doc : documents) {
            Map<String, Integer> termCount = new HashMap<>();
            Set<String> uniqueTerms = new HashSet<>();

            for (String term : doc) {
                termCount.put(term, termCount.getOrDefault(term, 0) + 1);
                uniqueTerms.add(term);
            }

            docTermCounts.add(termCount);

            for (String term : uniqueTerms) {
                docFreq.put(term, docFreq.getOrDefault(term, 0) + 1);
            }
        }
    }

    public Map<String, Double> transform(List<String> tokens) {
        Map<String, Integer> termCount = new HashMap<>();
        for (String term : tokens) {
            termCount.put(term, termCount.getOrDefault(term, 0) + 1);
        }

        Map<String, Double> tfidfVector = new HashMap<>();
        for (String term : termCount.keySet()) {
            double tf = termCount.get(term);
            double idf = Math.log((double) totalDocs / (docFreq.getOrDefault(term, 0) + 1));
            tfidfVector.put(term, tf * idf);
        }
        return tfidfVector;
    }

    public static void main(String[] args) {
        TfIdfVectorizer vectorizer = new TfIdfVectorizer();

        // 准备文档
        List<List<String>> documents = new ArrayList<>();
        documents.add(Arrays.asList("自然", "语言", "处理"));
        documents.add(Arrays.asList("深度", "学习", "模型"));
        documents.add(Arrays.asList("自然", "语言", "理解"));

        // 训练
        vectorizer.fit(documents);

        // 转换新文本
        List<String> query = Arrays.asList("自然", "语言", "技术");
        Map<String, Double> vector = vectorizer.transform(query);

        System.out.println("TF-IDF向量: " + vector);
    }
}
