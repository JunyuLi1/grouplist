package org.example.demo2.utils;

import java.util.concurrent.TimeUnit;

public interface Lock {

    // 获取分布式锁
    boolean tryLock(long timeout, TimeUnit unit);

    // 释放分布式锁
    void unlock();
}
