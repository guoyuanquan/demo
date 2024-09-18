package com.example.demo.controller.file;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * @Author：guoyq
 * @name：FileChange
 * @Date：2024/9/18 16:38
 * @Filename：FileChange
 */
@Component
public class FileChange {

//    接口获取二进制文件流，转成图片保存
    @PostConstruct
    public void downFile() throws Exception {
        FileChange fileChange= new FileChange();
        String url="http://74.10.28.118:1808/api-bj/shipping_converge/CBSJCX/CXTPXX/2c9180838d5974ac018dcfacf24c382b";
        fileChange.downLoadPhoto(url);
    }

    public void  downLoadPhoto(String photoUrl)throws Exception{

        //获取照片返回二进制流
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjkwYTcwYjU2LTJlYjItNGE3Yy04NzE3LTIyMWQxZjk1ZGMyOCJ9.ko3dppXoXlq4czBKHh6RzhTuh_Pni-6va20Cbliy5M7PCQe5UepxwCn50qT8FAYHWgb9KYZbb_uRAutPQGGcHA");
        ResponseEntity<byte[]> entity = restTemplate.exchange(photoUrl, HttpMethod.GET,new HttpEntity<>(headers), byte[].class);
        byte[] body = entity.getBody();
        InputStream sbs = new ByteArrayInputStream(body);

        File dest = new File("d:/file/a.jpg");
        if(!dest.exists()) {
            dest.createNewFile();
        }

        InputStream in = null;
        OutputStream out = null;

        in = new ByteArrayInputStream(body);
        out = new FileOutputStream(dest);

        byte []bt = new byte[1024];
        int length=0;
        while(	(length = in.read(bt)) !=-1) {
            //一次性写入文件a中
            out.write(bt,0,length);
        }

        if(null!=out) {
            out.close();
        }

    }
}
