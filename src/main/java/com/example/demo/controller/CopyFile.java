package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Thread.sleep;

@Slf4j
//@Component
public class CopyFile {

    @Value("${file.time}")
    private int fileTime;
    @Value("${file.targetPath}")
    private String targetPath;
    @Value("${file.sourcePath}")
    private String sourcePath;
    @Value("${file.num}")
    private int num;

    private SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSS");

    @PostConstruct
    public  void copyFile() throws IOException {
        for (int i=0;i<num;i++){

            this.copyMethod(i);
            try {
                sleep(fileTime);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }



    public void copyMethod(int i) throws IOException{
        //获取要复制的文件
        Calendar calendar=Calendar.getInstance();
        //获取系统当前时间并将其转换为string类型
        String fileName=sFormat.format(calendar.getTime());

        File oldfile=new File(sourcePath);
//        File oldfile=new File("D:\\123.txt");
//        log.info("1:"+oldfile);
        //文件输入流，用于读取要复制的文件
        FileInputStream fileInputStream = new FileInputStream(oldfile);
        //要生成的新文件（指定路径如果没有则创建）
        File newfile=new File(targetPath+i+"A"+fileName);
//        File newfile=new File("D:\\data\\topic\\"+i+"A"+fileName+".txt");
        log.info("2:"+newfile);
        System.out.println(newfile);
        //获取父目录
        File fileParent = newfile.getParentFile();
//        System.out.println(fileParent);
        //判断是否存在
        if (!fileParent.exists()) {
            // 创建父目录文件夹
            fileParent.mkdirs();
        }
        //判断文件是否存在
        if (!newfile.exists()) {
            //创建文件
            newfile.createNewFile();
        }

        //新文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream (newfile);
        byte[] buffer= new byte[1024];
        int len;
        //将文件流信息读取文件缓存区，如果读取结果不为-1就代表文件没有读取完毕，反之已经读取完毕
        while ((len=fileInputStream.read(buffer))!=-1) {
            fileOutputStream.write(buffer, 0, len);
            fileOutputStream.flush();
        }
        fileInputStream.close();
        fileOutputStream.close();
    }




}
