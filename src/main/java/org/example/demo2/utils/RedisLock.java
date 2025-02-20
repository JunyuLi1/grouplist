package org.example.demo2.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisLock implements Lock{


    private String lockName;
    private StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "lock:";

    public RedisLock(StringRedisTemplate redisTemplate, String lockName) {
        this.redisTemplate = redisTemplate;
        this.lockName = lockName;
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) {
        // 获取线程名称
        long ThreadId = Thread.currentThread().getId();
        System.out.println(ThreadId);

        // 如果不存在，说明锁获取成功，即没人用这个锁
        Boolean lockStatus = redisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + this.lockName, ThreadId+"", timeout, TimeUnit.SECONDS);

        // 解决自动拆箱可能空指针问题
        return Boolean.TRUE.equals(lockStatus);
    }

    @Override
    public void unlock() {
        redisTemplate.delete(KEY_PREFIX + this.lockName);
    }
}
