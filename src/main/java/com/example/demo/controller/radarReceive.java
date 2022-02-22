package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

@Slf4j
//@Component
public class radarReceive {
    public static void main(String[] args) throws IOException {
        ByteBuffer bufferQueue = ByteBuffer.allocateDirect((65535 + 13) * 10);
        ByteBuffer buffer = ByteBuffer.allocate(655350);
        /*
         * 向服务器端发送数据
         */
        //1.定义服务器的地址、端口号、数据
        InetAddress address=InetAddress.getByName("127.0.0.1");
        int port=12580;
        byte[] data=getHexBytes("AAAAAAAAAAAAAAAAAAAA");
        //2.创建数据报，包含发送的数据信息
        DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
        //3.创建DatagramSocket对象
        DatagramSocket socket=new DatagramSocket();
        //4.向服务器端发送数据报
        socket.send(packet);

        /*
         * 接收服务器端响应的数据
         */
        //1.创建数据报，用于接收服务器端响应的数据
        byte[] data2=new byte[1024];
        DatagramPacket packet2=new DatagramPacket(data2, data2.length);
        //2.接收服务器响应的数据
        socket.receive(packet2);
        //3.读取数据
        String reply=new String(data2, 0, packet2.getLength());

        log.info("响应数据："+data2);
        while (socket.isConnected()){
            System.out.println("我是客户端，服务器说："+reply);
        }


        //4.关闭资源
//        socket.close();
    }

    public static byte[] getHexBytes(String str){
        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

}
