package com.project.animal.config;

import com.project.animal.interceptors.LoginInterCeptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

//若没有这个注解，autowired将会报错
//该配置类是为了让拦截器注册进项目中
@Configuration
public class webConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterCeptor loginInterCeptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //此方法添加拦截器
        //登录和注册接口不用拦截，要放行
        registry.addInterceptor(loginInterCeptor).excludePathPatterns("/users/login"
                ,"/users/register"
                ,"/captcha"
                ,"/verify"
                ,"/notice/selectByStatus"
                ,"/notice/selectById"
                ,"/shelters/getAllSh"
                ,"/animal/pageList/**"
                ,"/activities/queryById"
                ,"/users/getUserNameById/*"
                ,"/users/getAvatarById/*"
                ,"/donations/selectAll"
                ,"/carousel/queryAll"
                ,"/email/send"
                ,"/email/checkCode");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                // 设置允许跨域请求的域名
//                .allowedOriginPatterns("*")
//                // 是否允许cookie
//                .allowCredentials(true)
//                // 设置允许的请求方式
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                // 设置允许的header属性
//                .allowedHeaders("*")
//                // 跨域允许时间
//                .maxAge(3600);
//    }

    // 如果上述方式无效，也可以尝试通过CorsFilter注册全局过滤器
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.setAllowCredentials(false);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
