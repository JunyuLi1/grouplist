package org.example.demo2.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo2.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;


// 用于验证用户状态的拦截器

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 验证JWT令牌合理性
        String token = request.getHeader("Authorization");
        if (token == null) {
            return false;
        }
//        token = token.substring(7); // 去掉 "Bearer " 前缀
        Map<String, Object> userMap = (Map<String, Object>) JwtToken.checkToken(token).get("user");
        User user = new User();
        user.setId((Integer) userMap.get("id"));
        user.setUserName((String) userMap.get("userName"));
        user.setPassword((String) userMap.get("password"));
        user.setEmail((String) userMap.get("email"));
        UserHolder.saveUser(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser(); // 从线程移除用户
    }
}
