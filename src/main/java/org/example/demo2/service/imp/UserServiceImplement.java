package org.example.demo2.service.imp;

import org.example.demo2.entity.User;
import org.example.demo2.entity.dto.UserDTO;
import org.example.demo2.mapper.UserMapper;
import org.example.demo2.repository.UserRepository;
import org.example.demo2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplement implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Integer userid) {
        return userMapper.selectUserById(userid); //调用userMapper中定义的函数
    }
}
