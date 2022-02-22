package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.apache.tomcat.jni.Time.sleep;

@Slf4j
//@Component
public class WriteFile {

    private SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");

    private String basedir = "/app/vsftpd/zww-gaw-bianjie/bianjie/test/";


//    @PostConstruct
    public void createFile() throws IOException {
        for (int i=0;i<100;i++){
                //获取要复制的文件30分钟
                Calendar calendar=Calendar.getInstance();
                //获取系统当前时间并将其转换为string类型
                String date=sFormat.format(calendar.getTime());
//                log.info(basedir + "\\" + date);
                File file = new File(basedir + "/" + date);
//                log.info(basedir + "/" + date);
                boolean b = file.createNewFile();
                if(b) {
                    Writer out = new FileWriter(file);
                    out.write(date);
                    out.close();
                }
            try {
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }
}
