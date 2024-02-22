package com.example.demo.controller.file;

import com.example.demo.controller.image.ImageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;

@Slf4j
//@Component
public class FileList {

    @Value("${wrj.filePath}")
    private String filePath;

    @Resource
    private ImageController imageController;

    @PostConstruct
    public void  getFile()throws Exception{
        File file = new File(filePath);
        listAll(file);
    }

    public void listAll(File dir)throws Exception{
        File[] files =dir.listFiles();
        for (int x=0;x<files.length;x++){
            if (files[x].isDirectory()){
                log.info(files[x].getAbsolutePath());
                listAll(files[x]);
            }
            else if (files[x].getName().endsWith(".JPG")){
                imageController.getImageDto(files[x].getAbsolutePath());
            }
        }
    }
}
