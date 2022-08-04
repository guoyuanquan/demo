package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
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
import java.net.URI;

@Slf4j
@Component
@EnableScheduling
public class WsController {

    WebSocketClient client = null;


    @Scheduled(initialDelay = 1 * 1000, fixedRate = 3 * 1000)
    public void initClient() {
        try {
            if (client == null) {
                String tocken = getMethod();
                log.info("token:"+tocken);
                String uri = "ws://172.28.17.53:8080/websocket?token="+tocken+"&userId=0&userName=hxyws" ;
//                String uri = "ws://172.28.17.53:8080/websocket?token="+tocken+"&userId=0&userName=hxyws" ;
                client = new WebSocketClient(new URI(uri), new Draft_6455()) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        log.info("WebSocket onOpen");
                    }
                    @Override
                    public void onMessage(String s) {
                        log.info("接收数据："+s);
                        if (JSON.isValid(s)) {
                            log.info("接收数据："+s);
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

}
