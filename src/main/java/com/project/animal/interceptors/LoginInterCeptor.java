package com.project.animal.interceptors;

import com.project.animal.utils.JwtUtil;
import com.project.animal.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

//写拦截器，为了看请求头里面是否携带正确的token
//注意此时还没有注册拦截器，要在webconfig配置类中注册拦截器
//该拦截器是为了不用每次访问都去设定判断是否携带正确的token，这里统一判断
@Component
public class LoginInterCeptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            //放行
            //此处打印的是{id=1, username=wang123}
            //System.out.println(claims);

            //放行前将其存入ThreadLocal中，为了不让每次调用都在形参位置获得用户名
            ThreadLocalUtil.set(claims);

            //获取redis中存储的token并判断是不是空，是空就不让登录
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String result = operations.get(token);
            if (result == null){
                throw new Exception();
            }
            return true;
        } catch (Exception e) {
            //解析失败，不放行，登录失败
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //调用接口结束后清除线程，防止内存泄露
        // 请求完成后的代码
        ThreadLocalUtil.remove();
    }
}
