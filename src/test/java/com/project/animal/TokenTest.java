package com.project.animal;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TokenTest {
    @Test
    public void testGen(){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","张三");
        String token = JWT.create()
                .withClaim("user", claims)//添加载荷
                //添加过期时间
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                //添加创建时间
                .withIssuedAt(new Date())
                //添加加密算法,配置密钥
                .sign(Algorithm.HMAC256("animals_token"));

        System.out.println(token);
    }

    @Test
    public void testParse(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTE4MzA2NzgsInVzZXIiOnsiaWQiOjEsInVzZXJuYW1lIjoi5byg5LiJIn19.cqUr9P4xPWglLOt7_JBOPITzHH9B8-MbHMFEzgnQQoQ";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("animals_token")).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);//验证token，生成一个解析后的jwt对象
        System.out.println(decodedJWT);
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));

        //如果篡改了头部和载荷部分的数据，那么验证失败
        //如果秘钥改了，验证失败
        //token过期
    }


    /**
     * 解密JWT令牌。
     *
     * 该方法使用预定义的秘钥对JWT令牌进行验证和解密，提取其中的用户ID（uid）、令牌创建时间（iat）和令牌过期时间（exp）。
     * 如果令牌过期或验证失败，将抛出运行时异常。
     *
     * @param token 待解密的JWT令牌。
     * @return 包含解密信息的Map，包括uid、create_at和expire_in。
     * @throws RuntimeException 如果令牌过期或验证失败，将抛出运行时异常。
     */
    public static Map<String, Object> decryptToken(String token) {
        // 预定义的秘钥，用于JWT令牌的验证。
        String secretKey = "SSSSSSSS";
        try {
            // 创建JWT验证器，使用HMAC256算法和预定义的秘钥。
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            // 验证令牌并获取解码后的JWT对象。
            DecodedJWT decodedJWT = verifier.verify(token);
            // 提取JWT中的声明信息。
            Map<String, Claim> result = decodedJWT.getClaims();
            // 创建一个Map用于存放解密后的信息。
            Map<String, Object> rv = new HashMap<>();
            // 从声明中提取用户ID、令牌创建时间和令牌过期时间，并存入结果Map中。
            rv.put("uid", result.get("uid"));
            rv.put("create_at", result.get("iat"));
            rv.put("expire_in", result.get("exp"));
            // 返回包含解密信息的Map。
            return rv;
        } catch (TokenExpiredException e) {
            // 令牌过期异常处理，抛出运行时异常并附带过期信息。
            throw new RuntimeException("Token过期");
        } catch (JWTVerificationException e) {
            // JWT验证异常处理，抛出运行时异常并附带验证失败信息。
            throw new RuntimeException("不合法Token");
        }
    }


}
