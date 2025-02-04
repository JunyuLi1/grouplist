package org.example.demo2.controller;

import org.example.demo2.entity.User;
import org.example.demo2.entity.dto.UserDTO;
import org.example.demo2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/query_id")
    public User getUserById(@RequestBody UserDTO userDTO) {
        return userService.getUserById(userDTO); //调用service层相关函数
    }
}
