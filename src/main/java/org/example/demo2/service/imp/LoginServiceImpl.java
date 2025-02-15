package org.example.demo2.service.imp;


import jakarta.annotation.Resource;
import org.example.demo2.entity.Response;
import org.example.demo2.entity.User;
import org.example.demo2.service.LoginService;
import org.example.demo2.utils.JwtToken;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

// 处理用户登录的service
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /*用于发送验证码*/
    public Response<String> sendCode(String phone){
        // 校验手机号

        // 生成验证码
        String code = String.format("%06d", new Random().nextInt(999999));

        // 保存到redis
        stringRedisTemplate.opsForValue().set("login:code:"+phone, code, 2, TimeUnit.MINUTES);

        // 发送验证码，返回成功
        return Response.success(code);
    }

    /*用于验证手机验证码*/
    public Response<String> verifyCode(String phone, String code, String userName, String password, String email, Integer id) {
        String storedCode = stringRedisTemplate.opsForValue().get("login:code:" + phone);
        if(storedCode != null && storedCode.equals(code)){
            User user = new User(userName, id, password, email);
            String token = JwtToken.generateToken(user);
            return Response.success(token);
        }
        return Response.success("failed");
    }
}
