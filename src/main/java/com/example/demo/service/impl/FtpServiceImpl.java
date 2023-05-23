package com.example.demo.service.impl;

import java.io.*;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.example.demo.service.FtpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FtpServiceImpl implements FtpService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;

    FTPClient ftp = connectFtpServer();

    @Override
    public Boolean uploadFile(InputStream inputStream, String fileName, String filePath) {
        logger.info("调用文件上传接口");
        // 定义保存结果
        boolean iaOk = false;
        // 初始化连接
        if (ftp == null){
            connectFtpServer();
        }
        if (ftp != null) {
            try {
                // 设置文件传输模式为二进制，可以保证传输的内容不会被改变
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();	//注：上传文件都为0字节，设置为被动模式即可
                // 跳转到指定路径，逐级跳转，不存在的话就创建再进入
                toPathOrCreateDir(ftp, filePath);
                // 上传文件 参数：上传后的文件名，输入流,,返回Boolean类型，上传成功返回true
                iaOk = ftp.storeFile(fileName, inputStream);
                // 关闭输入流
                inputStream.close();
                // 退出ftp
                ftp.logout();
            } catch (IOException e) {
                logger.error(e.toString());
            } finally {
                if (ftp.isConnected()) {
                    try {
                        // 断开ftp的连接
                        ftp.disconnect();
                    } catch (IOException ioe) {
                        logger.error(ioe.toString());
                    }
                }
            }
        }
        return iaOk;
    }

    @Override
    public void downloadFileTo(String ftpFilePath,String filename,String loacalpath) {
        if (ftp == null){
            connectFtpServer();
        }
        try{
            File loacalFile = new File(loacalpath+filename);
            OutputStream outputStream = new FileOutputStream(loacalFile);
//            FTPFile[] listFiles =ftp.listFiles();
//            if (listFiles.length>0){
//                for (int i=0;i<listFiles.length;i++){
//                    String filename = listFiles[i].getName();
//                    log.info("文件名："+filename);
//                }
//            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            ftp.retrieveFile(ftpFilePath+filename, outputStream);
            ftp.logout();

        } catch (Exception e) {
            logger.error("FTP文件下载失败！" + e.toString());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.toString());
                }
            }
        }
    }

    @Override
    public Boolean deleteFile(String ftpFilePath) {
        FTPClient ftp = connectFtpServer();
        boolean resu = false;
        try{
//			ftp.changeWorkingDirectory("文件路径");
//			ftp.dele("文件名称，如果写全路径就不需要切换目录了。");
//			deleteFile内同样实现了dele，只不多是对返回结果做了处理，相对方便一点
            resu = ftp.deleteFile(ftpFilePath);
            ftp.logout();
            return resu;
        } catch (Exception e) {
            logger.error("FTP文件删除失败！" + e.toString());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.toString());
                }
            }
        }
        return resu;
    }

    private FTPClient connectFtpServer() {
        // 创建FTPClient对象（对于连接ftp服务器，以及上传和上传都必须要用到一个对象）
        FTPClient ftpClient = new FTPClient();
        // 设置连接超时时间
        ftpClient.setConnectTimeout(1000 * 30);
        // 设置ftp字符集
        ftpClient.setControlEncoding("utf-8");
        // 设置被动模式，文件传输端口设置,否则文件上传不成功，也不报错
        ftpClient.enterLocalPassiveMode();
        try {
            // 定义返回的状态码
            int replyCode;
            // 连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
            ftpClient.connect(host);
            // 输入账号和密码进行登录
            ftpClient.login(username, password);
            // 接受状态码(如果成功，返回230，如果失败返回503)
            replyCode = ftpClient.getReplyCode();
            // 根据状态码检测ftp的连接，调用isPositiveCompletion(reply)-->如果连接成功返回true，否则返回false
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.info("connect ftp {} failed", host);
               // 说明连接失败，需要断开连接
                ftpClient.disconnect();
                return null;
            }
            logger.info("replyCode:" + replyCode);
        } catch (IOException e) {
            logger.error("connect fail:" + e.toString());
            return null;
        }
        return ftpClient;
    }

    private void toPathOrCreateDir(FTPClient ftp, String filePath) throws IOException {
        String[] dirs = filePath.split("/");
        for (String dir : dirs) {
            if (StringUtils.isEmpty(dir)) {
                continue;
            }

            if (!ftp.changeWorkingDirectory(dir)) {
                ftp.makeDirectory(dir);
                ftp.changeWorkingDirectory(dir);
            }
        }
    }

}

