package com.project.animal.controller;


import com.project.animal.constant.UploadConstant;
import com.project.animal.entity.Result;
import com.project.animal.utils.QiNiuUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin
public class FileUploadController {
    //模拟本地上传图片，返回result类型的字符串
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {

        //获取文件原始名
        String originalFilename = file.getOriginalFilename();
        //为了保证文件名唯一
        String fileName = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));

        file.transferTo(new File("D:\\Users\\不冷\\IdeaProjects\\animal\\src\\main\\resources\\static\\images\\"+fileName));
        return Result.success("url地址");
    }


    @PostMapping("/uploadQ")
    public Result<String> upload(MultipartFile file, String fileType) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return Result.error("文件名不能为空");
        }
        if (!this.validateFileName(fileName)) {
            return Result.error("文件名应仅包含汉字、字母、数字、划线和点号");
        }

        String url = "";
        if (fileType.equals(UploadConstant.IMAGE)) {
            url = new QiNiuUtil().upload(file, UploadConstant.IMAGE);
        } else if (fileType.equals(UploadConstant.FILE)) {
            url = new QiNiuUtil().upload(file, UploadConstant.FILE);
        } else {
            return Result.error("文件类型错误");
        }

        return Result.success(url);
    }

    /**
     * 验证文件名称：仅包含 汉字、字母、数字、下划线和点号
     *
     * @param fileName 文件名称
     * @return 返回true表示符合要求
     */
    private boolean validateFileName(String fileName) {
        //设置图片应该需要的的regex格式
        String regex = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\_\\-\\.]+$";
        return fileName.matches(regex);
    }
}
