package org.example.demo2.controller;

import org.example.demo2.entity.Response;
import org.example.demo2.entity.User;
import org.example.demo2.entity.dto.UserDTO;
import org.example.demo2.service.LoginService;
import org.example.demo2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;

    @GetMapping("/query_id")
    public User getUserById(@RequestBody UserDTO userDTO) {
        return userService.getUserById(userDTO); //调用service层相关函数
    }

    @GetMapping("/getLoginCode")
    public Response<String> getLoginCode(@RequestBody UserDTO userDTO) {
        return loginService.sendCode(userDTO.getPhone());
    }

    @GetMapping("/login")
    public Response<String> loginUser(@RequestBody UserDTO userDTO) {
        return loginService.verifyCode(userDTO.getPhone(), userDTO.getVerifyCode(), userDTO.getUserName(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getId());
    }
}
