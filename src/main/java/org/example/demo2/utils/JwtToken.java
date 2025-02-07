package org.example.demo2.utils;

import io.jsonwebtoken.*;
import org.example.demo2.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtToken {
    private static final String SECRET_KEY = "123456Abc@";  // Secret 密钥
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30 分钟

    public static String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", user);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        return jwtBuilder.compact();
    }

    public static Map<String, Object> checkToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return new HashMap<>(claims);
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token 已过期");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("不支持的 JWT");
        } catch (MalformedJwtException e) {
            throw new JwtException("JWT 格式错误");
        } catch (SignatureException e) {
            throw new JwtException("JWT 签名错误");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT 参数错误");
        }
    }

    public static void main(String[] args) {
        User user = new User("test",1,"testPWD", "example@example.com");
        String token = JwtToken.generateToken(user);
        System.out.println(token);
        Map<String, Object> claims = (Map<String, Object>) JwtToken.checkToken(token).get("user");
        System.out.println(claims);

    }

}
