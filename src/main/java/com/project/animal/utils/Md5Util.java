package com.project.animal.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Md5Util {
    public static void main(String[] args) {
        //密码明文
//        String password = "admin123";
//        //password经 MD5转换成的密文
//        String mwPassword = "AZICOnu9cyUFFvBp3xi1AA==";
//        String mw = transToMD5(password);
        //通常的密码验证： 将输入的密码明文转换成密文，再拿此密文和数据库中保存的密文相比较，进行密码验证
    }
    
    //使用MD5对字符串进行加密
    public static String transToMD5(String password) {
        try {
            //获取消息摘要对象
            MessageDigest md = MessageDigest.getInstance("md5");
            //通过MD5计算摘要，返回byte数组形式
            byte [] b=md.digest(password.getBytes());
            /*
             * Base64算法将 byte数组转换成可保存的字符串（a-z A-Z 0-9 * /）形式
             */
            String str = Base64.getEncoder().encodeToString(b);
            return str;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}