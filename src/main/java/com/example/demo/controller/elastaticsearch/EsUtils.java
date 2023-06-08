package com.example.demo.controller.elastaticsearch;

import com.example.demo.model.ImageExifDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

//@Component
public class EsUtils {

    @Autowired
    private EsClient esClient;

    public void save(ImageExifDto imageExifDto){
        RestHighLevelClient client = esClient.esclient();
        System.out.println(client);
        BulkResponse response=null;
        try {
            System.out.println("存放数据"+imageExifDto);
            String jsonUsers = new ObjectMapper().writeValueAsString(imageExifDto);
            response = client.bulk(new BulkRequest().add(new IndexRequest("index_seat_uav_image").id(UUID.randomUUID().toString()).source(jsonUsers, XContentType.JSON)), RequestOptions.DEFAULT);
            System.out.println(response.status());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
