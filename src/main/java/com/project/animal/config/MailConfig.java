package com.project.animal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(587); // 或你的邮件服务器端口
        mailSender.setUsername("2085886325@qq.com");
        mailSender.setPassword("lmaylyfofyevehdd");
        mailSender.setDefaultEncoding("UTF-8");

        return mailSender;
    }
}
