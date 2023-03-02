package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.util.HashMap;
import java.util.Map;


public class Http {
    public static void main(String[] args) throws Exception{
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.4321.sh/sms/template");
        httpPost.addHeader("Content-Type","application/json");
        Map<String,Object> map = new HashMap<>();
        map.put("apikey","N815041d38");
        map.put("secret","81504017caca46c8");
        map.put("sign_id",154081);
        map.put("mobile","13641246947,15210503934");
        map.put("template_id",124629);
        map.put("content","北斗轨迹丢失");
        String json = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(json,"UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String res = EntityUtils.toString(entity);
        System.out.println(res);
    }
}
