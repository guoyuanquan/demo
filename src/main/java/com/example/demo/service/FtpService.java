package com.example.demo.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface FtpService {

    Boolean uploadFile(InputStream inputStream, String fileName, String filePath);

    void downloadFileTo(String ftpFilePath,String filename,String localpath);

    Boolean deleteFile(String ftpFilePath);
}
