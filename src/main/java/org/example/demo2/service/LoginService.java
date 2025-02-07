package org.example.demo2.service;

import org.example.demo2.entity.Response;

public interface LoginService {
    Response<String> sendCode(String phone);

    Response<String> verifyCode(String phone, String code, String userName, String password, String email, Integer id);
}
