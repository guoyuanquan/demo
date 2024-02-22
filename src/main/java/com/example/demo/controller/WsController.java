package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.PostResult;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
//@Component
@EnableScheduling
public class WsController {

    WebSocketClient client = null;


//    @Scheduled(initialDelay = 1 * 1000, fixedRate = 3 * 1000)
//    @PostConstruct
    public void initClient(){
        try {
            if (client == null) {


                String uri = "ws://10.112.89.96:9016/?token=UHNhcp25P2LzN3nMjGP2LRIS" ;

                client = new WebSocketClient(new URI(uri)) {

                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        log.info("WebSocket onOpen");
                    }
                    @Override
                    public void onMessage(String s) {
//                            Object s1 = JSONObject.parseObject(s);
                        log.info(s);
//                            log.info("接收数据："+ s);
//                            if (JSON.isValid(s)) {
//                                log.info("接收数据："+s1);
//                            }



                    }
                    @Override
                    public void onMessage(ByteBuffer bytes) {

                        try {
                            System.out.println(conver16HexStr(bytes.array()));
                        } catch (Exception e) {

                            log.info("出现异常");

                        }
                    }
                    @Override
                    public void onClose(int i, String s, boolean b) {
                        log.info("WebSocket onClose {}",s);
                    }
                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                };
                // 修复自动断线
                client.setConnectionLostTimeout(0);
                client.connect();
            }
        } catch (Exception e) {
            log.error("最外层捕获到异常", e);
            client = null;
        }
    }


    public static String conver2HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(Long.toString(b[i] & 0xff, 2) );
        }
        return result.toString().substring(0, result.length() - 1);
    }

    public static String conver16HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xff) < 0x10){
                result.append("0");
            }
            result.append(Long.toString(b[i] & 0xff, 16));
        }
        return result.toString().toUpperCase();
    }

    public String getMethod(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://172.28.17.53:8080/api/newToken.json";
//        String url = "http://172.28.17.53:8080/api/newToken.json";
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("userName","hxyws");
        params.add("password","123456");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.queryParams(params).build().encode().toUri();
        ResponseEntity<PostResult> response = restTemplate.getForEntity(uri,PostResult.class);
        return response.getBody().getData();
    }

    public static void main(String args[]){
        String aa="{\"shipId\":\"\\u743c\\u6d770202118\",\"devideNo\":\"15004229\",\"shipType\":null,\"jobType\":\"\\u6e14\\u4e1a\",\"jobWay\":\"\\u6e14\\u4e1a\\u6355\\u635e\",\"ownerType\":\"0\",\"ownerName\":\"\\u6797\\u5c14\\u5174\",\"ownerCompany\":null,\"ownerTelNo\":\"13519855072\",\"policeStationId\":\"001003002\",\"shipLength\":\"8.70\",\"shipWidth\":\"1.50\",\"shipMaterial\":\"\\u73bb\\u7483\\u94a2\\u8d28\",\"createTime\":null,\"modifyTime\":\"1664518230216\"}";
        JSONObject s1 = JSONObject.parseObject(aa);
        System.out.println(s1);
    }
}
