package com.project.animal.utils;

import com.google.gson.Gson;
import com.project.animal.constant.UploadConstant;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.UUID;

/**
 * Description:
 *
 * @author: bright
 * @date:Created in 2020/12/3 17:39
 */
public class QiNiuUtil {

    /**
     * 将图片上传到七牛云
     */
    public String upload(MultipartFile file, String fileType) throws Exception {
        // zone0华东区域,zone1是华北区域,zone2是华南区域
        Configuration cfg = new Configuration(Region.region1());
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传
        Auth auth = Auth.create(UploadConstant.accessKey, UploadConstant.secretKey);

//        // 创建临时文件
//        File tempFile = File.createTempFile(fileName, null);
//        // 将输入流中的数据写入临时文件
//        file.transferTo(tempFile);

        InputStream fileInputStream = file.getInputStream();

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        //设置上传后的文件名称
        String originalFilename = file.getOriginalFilename();
        String key = "";
        //上传凭证，参数是空间名
        String upToken = auth.uploadToken(UploadConstant.bucketName);
        //设置上传区域：域名
        String path = UploadConstant.domainName;
        Response response = null;
        if (fileType.equals(UploadConstant.IMAGE)) {
            key = UploadConstant.imgUpPath+ UUID.randomUUID().toString()
                    + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            response = uploadManager.put(fileInputStream,key, upToken, null, null);
        }else if (fileType.equals(UploadConstant.FILE)) {
            key =UploadConstant.fileUpPath+ UUID.randomUUID().toString()
                    + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            response = uploadManager.put(fileInputStream,key, upToken, null, null);
        }
        //删除临时文件
        // tempFile.delete();

        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return path + putRet.key;
    }
}

