package com.isolate.tanhua.mytanhuasso.utils;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ping on 2020/5/2.
 */
public class JwtUtils {
    /**
     * 私钥
     */
    private static final String JWT_SECRET = "76bd425b6f29f7fcc2e0bfc286043df1";

    /**
     *生成 token
     * @param claims 自定义Payload键值对
     * @param TTLMillis 过期时间,单位秒(s)
     * @return
     */
    public static String createToken(String audience, String subject, Map<String, Object> claims, Long TTLMillis) {
        //  生成签名密钥
        Key signingKey = generateKey();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //  生成JWT的时间
        Long nowMillis = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                // 设置header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // 生成签名的时间
                .setIssuedAt(new Date(nowMillis))
                // 设置payload的键值对
                .setClaims(claims)
                // JWT签发者
                .setIssuer("isolate")
                // 代表这个JWT的主体，即它的所有人
                .setSubject(subject)
                // 代表这个JWT的接收对象
                .setAudience(audience)
                // 签名需要的算法和key
                .signWith(signatureAlgorithm, signingKey);

        //  添加Token过期时间
        if (TTLMillis != null && TTLMillis >= 0) {
            Long expMillis = nowMillis + TTLMillis * 1000;
            Date exp = new Date(expMillis);
            //  设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 生成 token ，没有audience(接收者),subject(面向用户)
     * @param claims payload(用户信息参数)
     * @param TTLMillis 过期时间,单位秒(s)
     * @return
     */
    public static String createToken(Map<String, Object> claims, Long TTLMillis) {
        return createToken(null, null, claims, TTLMillis);
    }

    /**
     * 生成 token ，没有过期时间
     *
     * @param claims 自定义payload的键值对
     * @return
     */
    public static String createToken(String audience, String subject, Map<String, Object> claims) {
        return createToken(audience, subject, claims, null);
    }


    /**
     * 生成 token ，没有payload
     * @param audience
     * @param subject
     * @param TTLMillis 过期时间,单位秒
     * @return
     */
    public static String createToken(String audience, String subject, Long TTLMillis) {
        return createToken(audience, subject, null, TTLMillis);
    }

    /**
     * 解密 jwt
     * @param token 请求头中authorization的token
     * @return 返回生成token是的payload
     */
    public static Claims decodeToken(String token) {
        if (token == null) {
            return null;
        }
        try {
            return Jwts.parser()
                    //  此处的key要与之前创建的key一致
                    .setSigningKey(generateKey())
                    .parseClaimsJws(token)
                    .getBody();

        }catch (ExpiredJwtException e){
            return null;
        }
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    private static SecretKey generateKey() {
        //  生成签名密钥
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static void main(String[] args) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", 1L);
        String token = JwtUtils.createToken("admin", "isolate", claims, 86400L);
        System.out.println(token);
        Map<String, Object> claim = JwtUtils.decodeToken(token);
        System.out.println(claim);
        System.out.println(claim.get("user_id"));
    }
}
