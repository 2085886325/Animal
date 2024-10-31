package com.project.animal.controller;


import com.project.animal.entity.Result;
import com.project.animal.entity.Users;
import com.project.animal.service.IUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/email")
@CrossOrigin//允许跨域
public class SendEmailController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IUsersService userService;

    @PostMapping("/send")
    public Result send(@RequestBody Map<String,String> data){
        String email = data.get("email");
        String username = data.get("username");
        //通过用户名获取用户信息
        Users user = userService.findByUserName(username);
        if (user==null||!(user.getEmail().equals(email))){
            return Result.error("用户不存在或用户与邮箱不匹配");
        }

        //验证邮箱是不是正确的邮箱
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            return Result.error("邮箱格式不正确");
        }


        SimpleMailMessage message = new SimpleMailMessage();
        Random random = new Random();
        //随机生成6位数字验证码
        int codeValue = random.nextInt(899999) + 100000;
        message.setFrom("2085886325@qq.com");
        message.setTo(email);
        message.setSubject("流浪动物管理系统验证码");
        message.setText("邮箱验证码为: " + codeValue +",五分钟内有效，请勿发送给他人");
        try {
            mailSender.send(message);
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(email, String.valueOf(codeValue),300, TimeUnit.SECONDS);
            log.info("验证码邮件已发送。");
            return Result.success("验证码已发送");
        } catch (Exception e) {
            log.error("发送验证码邮件时发生异常了！", e);
            return Result.error("发送验证码邮件时发生异常了"+e.getMessage());
        }
//        return Result.success("验证码已发送");
    }

    //用户输入的验证码与后台发送的验证码对比 检验验证码
    @PostMapping("/checkCode")
    public Result checkCode(@RequestBody Map<String,String> data){
//        System.out.println(data);
        String email = data.get("email");
        String verificationCode = data.get("verificationCode");
        String username = data.get("username");
        if(email==null||verificationCode==null||username==null){
            return Result.error("参数错误");
        }
        //通过用户名获取用户信息
        Users user = userService.findByUserName(username);
        if (user==null||!(user.getEmail().equals(email))){
            return Result.error("用户不存在或用户与邮箱不匹配");
        }


        if (redisTemplate == null && !redisTemplate.hasKey(email)) {
            return Result.error("验证码已过期");
        }
        //System.out.println(redisTemplate.opsForValue().get("code"));
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String sendCode = operations.get(email);
        if(sendCode!=null&&sendCode.equals(verificationCode)){
            //验证码正确，重置用户的密码为八位随机数
            String pwd = String.valueOf(new Random().nextInt(89999999) + 10000000);
            userService.resetPwd(pwd,user.getUserid());
            return Result.success(pwd);
        }else{
            return Result.error("验证码错误或已过期");
        }
    }

    @PostMapping("/sendForEmail")
    public Result sendForEmail(@RequestBody Map<String,String> data){
        String email = data.get("email");
        String username = data.get("username");
        //通过用户名获取用户信息
        Users user = userService.findByUserName(username);
        if (user==null){
            return Result.error("用户不存在");
        }
        //验证邮箱是不是正确的邮箱
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            return Result.error("邮箱格式不正确");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        Random random = new Random();
        //随机生成6位数字验证码
        int codei = random.nextInt(899999) + 100000;
        message.setFrom("2085886325@qq.com");
        message.setTo(email);
        message.setSubject("流浪动物管理系统验证码");
        message.setText("邮箱验证码为: " + codei +",五分钟内有效，请勿发送给他人");
        try {
            mailSender.send(message);
            String code = String.valueOf(codei);
            redisTemplate.opsForValue().set("email", code ,300, TimeUnit.SECONDS);
            log.info("验证码邮件已发送。");
            return Result.success("验证码已发送");
        } catch (Exception e) {
            log.error("发送验证码邮件时发生异常了！", e);
            return Result.error("发送验证码邮件时发生异常了"+e.getMessage());
        }
//        return Result.success("验证码已发送");
    }




    //检查验证码是否正确  暂时弃用
    @PostMapping("/checkCodeByEmail")
    //前端传入邮箱和验证码 后端通过邮箱获取到redis中key为邮箱，值为code的内容进行比对
    public Result checkCodeByEmail(@RequestBody Map<String,String> data){
         String email = data.get("email");
         String verificationCode = data.get("verificationCode");
         if(email==null||verificationCode==null){
             return Result.error("参数错误");
         }
         if (redisTemplate == null && !redisTemplate.hasKey(email)) {
             return Result.error("验证码已过期");
         }
         if(redisTemplate.opsForValue().get(email).equals(verificationCode)){
             return Result.success("验证码正确");
         }else{
             return Result.error("验证码错误或已过期");
         }
    }

}