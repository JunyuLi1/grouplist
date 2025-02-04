package org.example.demo2.service;

import org.example.demo2.entity.User;
import org.example.demo2.entity.dto.UserDTO;


public interface UserService {
    User getUserById(UserDTO userDTO);
}
