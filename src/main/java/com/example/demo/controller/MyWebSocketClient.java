package com.example.demo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;



public class MyWebSocketClient extends WebSocketClient implements Runnable {
    public MyWebSocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        System.out.println("握手...");
        for(Iterator<String> it = shake.iterateHttpFields(); it.hasNext();) {
            String key = it.next();
            System.out.println(key+":"+shake.getFieldValue(key));
        }
    }

    @Override
    public void onMessage(String paramString) {
        System.out.println("接收到消息："+paramString);
    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        System.out.println("关闭...");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("异常"+e);

    }

    public static void main(String[] args) {
        try {
            String address = "ws://172.28.17.53:8080/websocket?token=A4DADF38CC90D1E62CC8556650954C5C&userId=0&userName=hnssgx";
            MyWebSocketClient client = new MyWebSocketClient(address);
            client.connect();
            Thread.sleep(3);
            System.out.println(address);
//            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
//                System.out.println("还没有打开");
//            }
            System.out.println("建立websocket连接");
            //client.send("asd");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
