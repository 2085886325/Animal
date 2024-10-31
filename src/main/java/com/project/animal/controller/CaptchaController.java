package com.project.animal.controller;

import com.project.animal.config.CaptchaConfig;
import com.project.animal.entity.Result;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin
@RestController
@RequestMapping
public class CaptchaController {

    // 生成验证码图片并返回
    @RequestMapping("/captcha")
    public Result<String> captchaImage(@RequestParam String captchaId){
        // 生成UUID作为验证码的唯一标识
        // String captchaId = UUID.randomUUID().toString();
        // 指定验证码长度和个数
        Captcha captcha = new SpecCaptcha(120, 40, 5);
        captcha.setCharType(SpecCaptcha.TYPE_NUM_AND_UPPER);
        // 将验证码文本存储到captchaConfig中
        ConcurrentHashMap<String, String> captchaMap = CaptchaConfig.getCaptchaMap();
        captchaMap.put(captchaId, captcha.text().toLowerCase());
//        System.out.println(captchaMap);
        // 判断map是否大于10，大于10则清空map
        if (captchaMap.size()>=5){
            //清空map
            captchaMap.clear();
        }
        //CaptchaUtil
        return Result.success(captcha.toBase64());
    }

    // 验证验证码是否正确
    @GetMapping("/verify")
    public Result<String> verifyCaptcha(@RequestParam String captchaId,@RequestParam String code) {
        // 从captchaConfig中获取验证码文本
        ConcurrentHashMap<String, String> captchaMap = CaptchaConfig.getCaptchaMap();
        String correctCaptcha = captchaMap.get(captchaId);

        // 如果验证码不存在，返回错误
        if (correctCaptcha == null|| correctCaptcha.isEmpty()) {
            return Result.error("验证码已过期或不存在");
        }

        // 验证用户输入的验证码是否正确
        boolean isSuccess = correctCaptcha.equals(code.toLowerCase());

        // 无论验证是否成功，都清除captchaConfig中的验证码，防止重复使用
//        captchaMap.remove(captchaId);
        CaptchaConfig.clearCaptcha(captchaId);
        //清空map
        captchaMap.clear();

        return Result.success(isSuccess?"验证成功":"验证失败");
    }
}