package com.example.demo.controller.elastaticsearch;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class EsClient {

    @Value("${es.hosts}")
    private String hosts;
    @Value("${es.username}")
    private String userName;
    @Value("${es.password}")
    private String password;
    @Value("${es.connectTimeout}")
    private int connectTimeout;
    @Value("${es.maxConnection}")
    private int maxConnection;


    public RestHighLevelClient esclient(){
        String[] httpHosts = hosts.split(",");
        HttpHost[] hosts = new HttpHost[httpHosts.length];
        for (int i = 0; i < httpHosts.length; i++) {
            if (!StrUtil.isEmpty(httpHosts[i])) {
                if (httpHosts[i].contains(":")) {
                    String uri[] = httpHosts[i].split(":");
                    hosts[i] = new HttpHost(uri[0], Integer.parseInt(uri[1]), "http");
                } else {
                    hosts[i] = new HttpHost(httpHosts[i], 9200, "http");
                }
            }
        }
        //判断，如果未配置用户名，则进行无用户名密码连接，配置了用户名，则进行用户名密码连接
        if (StrUtil.isEmpty(userName)) {
            RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(hosts));
            return client;
        } else {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    //es账号密码
                    new UsernamePasswordCredentials(userName, password));
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(hosts)
                            .setHttpClientConfigCallback((httpClientBuilder) -> {
                                httpClientBuilder.setMaxConnTotal(maxConnection);
                                httpClientBuilder.disableAuthCaching();
                                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

                                return httpClientBuilder;
                            })
                            .setRequestConfigCallback(builder -> {
                                builder.setConnectTimeout(connectTimeout);

                                return builder;
                            })
            );
            return client;
        }

    }

}
