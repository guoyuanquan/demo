package com.example.demo.controller;

import com.example.demo.service.FtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

@Slf4j
//@Component
public class FtpController {

    @Value("${ftp.upload.filepath}")
    private String uploadFilePath;
    @Value("${ftp.upload.filename}")
    private String uploadFileName;
    @Value("${ftp.download.filepath}")
    private String downloadFilePath;
    @Value("${ftp.download.filename}")
    private String downloadFileName;

    @Autowired
    private FtpService ftpService;

    void uploadFile() throws IOException {
        File file = new File(uploadFilePath);
            InputStream inputStream = new FileInputStream(file);
        ftpService.uploadFile(inputStream,uploadFileName,uploadFilePath);
    }

    @PostConstruct
    void downloadFile(){
        ftpService.downloadFileTo(downloadFilePath);
    }
}
