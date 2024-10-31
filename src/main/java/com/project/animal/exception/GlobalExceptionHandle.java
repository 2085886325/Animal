package com.project.animal.exception;

import com.project.animal.entity.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//全局异常处理
@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(Exception.class)
    public Result exceptionHandle(Exception e){
        e.printStackTrace();//打印到控制台
        //正则判断输出内容
        return Result.error(StringUtils.hasLength(e.getMessage())? "出现如下异常："+e.getMessage():"操作失败，出现异常");
    }
}
