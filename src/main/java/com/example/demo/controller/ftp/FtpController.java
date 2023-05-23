package com.example.demo.controller.ftp;

import com.example.demo.service.FtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Value("${ftp.download.localpath}")
    private String localpath;

    @Autowired
    private FtpService ftpService;

    void uploadFile() throws IOException {
        File file = new File(uploadFilePath);
            InputStream inputStream = new FileInputStream(file);
        ftpService.uploadFile(inputStream,uploadFileName,uploadFilePath);
    }

//    @Scheduled(initialDelay = 5000,fixedDelay = 60*1000*60)
    void downloadFile(){
        ftpService.downloadFileTo(downloadFilePath,downloadFileName,localpath);
    }
}
