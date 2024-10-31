package com.project.animal.config;

import java.util.concurrent.ConcurrentHashMap;
  
public class CaptchaConfig {  
    // 使用ConcurrentHashMap来存储验证码  
    private static final ConcurrentHashMap<String, String> CAPTCHA_MAP = new ConcurrentHashMap<>();  
  
    // 获取验证码Map  
    public static ConcurrentHashMap<String, String> getCaptchaMap() {  
        return CAPTCHA_MAP;  
    }  
  
    // 清除验证码  
    public static void clearCaptcha(String key) {  
        CAPTCHA_MAP.remove(key);  
    }  
}