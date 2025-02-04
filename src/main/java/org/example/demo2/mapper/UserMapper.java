package org.example.demo2.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.demo2.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectUserById(Integer id); //定义mybatis的sql语句映射, 要名称对应
}
