package com.tdwl.wife.sql.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.net.ftp.FtpClient;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName UploadHelper
 * @Description 图片操作
 * @Author HanKeQi
 * @Date 2018/7/19 上午11:59
 * @Version 1.0.0
 **/
@Component
public class UploadHelper {

    private static final Logger _LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    //初始化 TODO
    private ApplicationContext applicationContext;

    private String remotefilename;

    private FtpClient ftpClient;

    public String uploadMultipartFile(MultipartFile file, String savePath, String goodsId) {
        try {
            String fileName = getImageEndName(file.getOriginalFilename(), goodsId);
            Pattern p = Pattern.compile(file.getContentType());
//            String upLoadFileType  = applicationContext.getEnvironment().getProperty("upload.file.type");
//            Matcher m = p.matcher(upLoadFileType);
//            if ( m.find()) {
            connectServer(savePath);
            //String path = applicationContext.getEnvironment().getProperty("ftp.path");
            String path = "/data/static/image/";
            upload(file, path + savePath + "/" + fileName);
            StringBuilder builder = new StringBuilder();
            //String imgUrlStart = applicationContext.getEnvironment().getProperty("img.domain.url");
            String imgUrlStart= "http://47.96.10.254";
            builder.append(imgUrlStart).append("/");
            builder.append(savePath).append("/");
            builder.append(fileName);
            return builder.toString();
            //}
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String upLoadImageFile(File myFile, String savePath){
        try {
            String myFileFileName = getImageEndName(myFile.getName(), null);
            String imgUrlStart = applicationContext.getEnvironment().getProperty("img.domain.url");
            String url = imgUrlStart+"/"+savePath+"/"+myFileFileName;
            Pattern p = Pattern.compile(getImageFileType(myFile));
            String upLoadFileType  = applicationContext.getEnvironment().getProperty("upload.file.type");
            Matcher m = p.matcher(upLoadFileType);
            if (m.find()){
                connectServer(savePath);
                upload(myFile, "/"+savePath+"/"+myFileFileName);
                return url;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void connectServer(String path) throws Exception {
        if(ftpClient == null){
            ftpClient = FtpClient.create();
            ftpClient.setConnectTimeout(30000);
            ftpClient.setReadTimeout(35000);
        }
        if(!ftpClient.isConnected()){
//            String serviceIp = applicationContext.getEnvironment().getProperty("img.service.ip");
//            String port = applicationContext.getEnvironment().getProperty("ftp.port");
            String serviceIp = "47.96.10.254";
            String port = "1315";

            SocketAddress addr = new InetSocketAddress(serviceIp, Integer.valueOf(port));
            ftpClient.connect(addr);
//            String userName = applicationContext.getEnvironment().getProperty("ftp.username");
//            String userPwd = applicationContext.getEnvironment().getProperty("ftp.userpwd");
            String userName = "checkftp";
            String userPwd = "w$TZu0PkmiKWfmej";
            ftpClient.login(userName, userPwd.toCharArray());
            ftpClient.setType(FtpClient.TransferType.BINARY);
            _LOGGER.info("/******************login success!*********/");
        }
        if (path.length() > 0) {
            ftpClient.list(path); //处理权限
            if (!isDirExist(path))
                createDir(path);//创建目录
        }
    }

    private boolean isDirExist(String savePath) throws Exception{
        try {
            ftpClient.changeDirectory(savePath);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void createDir(String savePath) throws Exception {
        if (!StringUtils.isEmpty(savePath)) {
            ftpClient.setAsciiType();
            String pathName[] = { savePath };
            if (savePath.contains("/")) {
                pathName = savePath.split("/");
            }
            for (int i = 0; i < pathName.length; i++) {
                String key = pathName[i];
                if (!StringUtils.isEmpty(key) && !isDirExist(key)) {
                    ftpClient.makeDirectory(key);
                    ftpClient.changeDirectory(key);
                }
            }
            ftpClient.setBinaryType();
        }
    }

    private String getImageFileType(File f) throws Exception{
        if (isImage(f)){
            ImageInputStream iis = ImageIO.createImageInputStream(f);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()){
                return null;
            }
            ImageReader reader = iter.next();
            iis.close();
            return reader.getFormatName();
        }
        return null;
    }

    private void upload(File file_in, String remoteFile) throws Exception{
        remotefilename = remoteFile;
        OutputStream os =  ftpClient.putFileStream(remotefilename);//将远程文件加入输出流中
        FileInputStream is = new FileInputStream(file_in);//获取本地文件的输入流
        byte[] bytes = new byte[1024];
        int c;
        while ((c = is.read(bytes)) != -1) {
            os.write(bytes, 0, c);
        }
        os.close();
        is.close();
        _LOGGER.info("upload success");
    }

    private String getImageEndName(String fileName,String goodsId) {
        Random rd = new Random();
        String randomStr = String.valueOf(Math.abs(rd.nextInt()) % 20 + 1) + Math.abs(rd.nextInt()) % 50 + 1;
        String imageNameDate = DateTimeHelper.getDateToFormatter(new Date(), "yyMMddHHmmssSSS");
        String fileType[] = fileName.split("\\.");
        StringBuilder imageNameBuilder =new StringBuilder();
        if (!StringUtils.isEmpty(goodsId)){
            imageNameBuilder.append(goodsId);
        }
        imageNameBuilder.append(imageNameDate).append(randomStr);
        imageNameBuilder.append(".").append(fileType[1]);
        return imageNameBuilder.toString();
    }

    public boolean isImage(File file) throws Exception{
        BufferedImage bufreader = ImageIO.read(file);
        int width = bufreader.getWidth();
        int height = bufreader.getHeight();
        if(width>0 || height>0){
            return true;
        }
        return false;
    }

    private void upload(MultipartFile fileIn, String remoteFile) throws Exception {
        OutputStream os = null;
        InputStream is = null;
        remotefilename=remoteFile;
        os = ftpClient.putFileStream(remotefilename);
        is = fileIn.getInputStream();
        byte[] bytes = new byte[1024];
        int c;
        while ((c = is.read(bytes)) != -1) {
            os.write(bytes, 0, c);
        }
        os.close();
        is.close();
        _LOGGER.info("upload success");
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


}
