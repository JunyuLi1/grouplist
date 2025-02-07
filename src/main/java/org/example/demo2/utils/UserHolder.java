package org.example.demo2.utils;

// 定义用户用户验证的ThreadLocal

import org.example.demo2.entity.User;

public class UserHolder {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void saveUser(User sessionUser) {
        userThreadLocal.set(sessionUser);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void removeUser() {
        userThreadLocal.remove();
    }
}
