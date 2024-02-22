package com.example.demo.controller.request;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author：guoyq
 * @name：Posts
 * @Date：2024/2/22 11:32
 * @Filename：Posts
 */
@Slf4j
@Component
public class Posts {
        private static PoolingHttpClientConnectionManager connMgr;
        private static RequestConfig requestConfig;
        public String gabdb = "ax_data_gab";




        @PostConstruct
        public void sendPost(){
        String id="1534431269301735426";
        String url="http://74.10.28.62:81/seat-resource/seat/file/resource/ycm/cbzp/2E74GKKHYOKEI1JXP4BB8D3RD.jpg";
        String fileName="2E74GKKHYOKEI1JXP4BB8D3RD.jpg";
        String tableName="ship_picture";
        File file = getFile(url,fileName);
        HttpResponse response = sendMultipartData(file);
        HttpEntity responseEntity= response.getEntity();
        String responseBody = null;
        try {
            responseBody = EntityUtils.toString(responseEntity,"UTF-8");
            log.info("返回结果:"+responseBody);
            ResultEntity result= JSONObject.parseObject(responseBody,ResultEntity.class);
            int statusCode = result.getStatusCode();
            String msg = result.getStatusMessage();
            if (statusCode==0){
                log.info("信息："+msg);
                List<Map> dataList= JSONObject.parseArray(result.getDatas(),Map.class);
                String code=dataList.get(0).get("code").toString();
            }else {
                log.info(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        file.delete();
    }

        //获取图片下载放到本地临时文件夹
        public File getFile(String url,String fileName)  {
        File file=new File("/tmp/"+fileName);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        // 发送请求并获取响应
        try{
            CloseableHttpResponse response = httpClient.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();
            // 将文件流保存到本地文件中
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            // 关闭连接
            EntityUtils.consume(response.getEntity());
            response.close();
            httpClient.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return file;
    }


        //发送文件
        public HttpResponse sendMultipartData(File file) {
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost post = new HttpPost("https://10.2.120.146:50295/shipping_converge/JGSJHJ/ZPSJHJ");
        String boundary="--------------------"+ UUID.randomUUID().toString();
        post.setHeader("Content-Type", "multipart/form-data; boundary="+boundary);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName("UTF-8"));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setBoundary(boundary);
        builder.addPart("multipartFile",new FileBody(file));
        builder.addBinaryBody(file.getName(),file, ContentType.create("application/octet-stream"), file.getName());
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        HttpResponse response = null;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

        /**
         * 创建SSL安全连接
         *
         * @return
         */
        private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }



}
