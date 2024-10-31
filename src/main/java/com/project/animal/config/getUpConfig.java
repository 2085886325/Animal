package com.project.animal.config;

import com.project.animal.constant.UploadConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * 加载配置
 * @author bright
 */
@Component
@ConfigurationProperties(prefix = "oss.qiniu")
@Data
public class getUpConfig {
    /**
     * AccessKey
     */
    private String accessKey;
    /**
     * SecretKey
     */
    private String secretKey;
    /**
     * 图片存储空间名
     */
    private String bucketName;
    /**
     * 图片外链
     */
    private String domainName;
    /**
     * 图片上传路径
     */
    private String imgUpPath;
    /**
     * 文件上传路径
     */
    private String fileUpPath;

    //从配置文件中加载配置并赋值给UploadConstant常量类
    @Bean
    public void init() {
        UploadConstant.accessKey = this.accessKey;
        UploadConstant.secretKey = this.secretKey;
        UploadConstant.bucketName = this.bucketName;
        UploadConstant.domainName = this.domainName;
        UploadConstant.imgUpPath = this.imgUpPath;
        UploadConstant.fileUpPath = this.fileUpPath;
    }
}
