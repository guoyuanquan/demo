package com.example.demo.controller.bigdata.embedding;

/**
 * @Author：guoyq
 * @name：BertEmbedding
 * @Date：2025/7/4 15:17
 * @Describetion:
 */
import ai.onnxruntime.*;

import java.nio.IntBuffer;
import java.util.*;

public class BertEmbeddingONNX {
    public static void main(String[] args) throws Exception {
        // 1. 加载模型（需提前下载ONNX格式的BERT模型）
        String modelPath = "bert-base-chinese.onnx";
        OrtEnvironment env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions options = new OrtSession.SessionOptions();
        OrtSession session = env.createSession(modelPath, options);

        // 2. 准备输入（实际应用需分词和ID转换）
        // 输入格式: [batch_size, sequence_length]
        int[] inputIds = {101, 100, 100, 100, 102}; // [CLS] + tokens + [SEP]
        long[] inputShape = {1, inputIds.length};

        // 3. 创建输入张量
        OnnxTensor inputTensor = OnnxTensor.createTensor(env, IntBuffer.wrap(inputIds), inputShape);

        // 4. 准备输入映射
        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input_ids", inputTensor);

        // 5. 运行推理
        try (OrtSession.Result results = session.run(inputs)) {
            // 6. 获取输出（假设输出名为"last_hidden_state"）
            OnnxValue output = results.get(0);
            float[][][] embeddings = (float[][][]) output.getValue();

            // 7. 提取[CLS]位置的向量作为句表示
            float[] clsEmbedding = embeddings[0][0];
            System.out.println("BERT向量维度: " + clsEmbedding.length);
        }
    }
}
