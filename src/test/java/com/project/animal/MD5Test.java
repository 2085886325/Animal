package com.project.animal;

import com.project.animal.utils.Md5Util;
import org.junit.jupiter.api.Test;

public class MD5Test {
    @Test
    public void testMd5() {
        String str = "123456";
        String md5 = Md5Util.transToMD5(str);
        System.out.println(md5);
    }


//    @Test
//    public void love() {
//        //打印爱心开始(制表位为两个空格)
//        //第一步：打印出上半部分
//        for (int i = 0; i < 3; i++) {//外层循环
//            for (int k = 0; k < 3 - i; k++) {//利用循环打印出左边空格
//                System.out.print("  ");
//            }
//            for (int j = 0; j < (2 * i + 7); j++) {//内层循环
//                if (i == 0 && (j == 2 || j == 3 || j == 4)) {//利用判断打印中间空格
//                    System.out.print("  ");//第一行中间空格
//                    //break;
//                } else if (i == 1 && (j == 4)) {//利用判断打印中间空格
//                    System.out.print("  ");//第二行中间空格
//                } else {
//                    System.out.print("* ");//否则打印出上半部分主体结构
//                }
//            }
//            System.out.println();//输入换行效果
//        }
//        //第二步：打印出下半部分
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j <= i + 1; j++) {//利用循环打印出左边空格
//                System.out.print("  ");
//            }
//            for (int k = 0; k < (9 - 2 * i); k++) {//打印出下半部分主体结构
//                System.out.print("* ");
//            }
//            System.out.println("");//换行效果
//        }
//        //打印爱心注释结束
//    }


}
