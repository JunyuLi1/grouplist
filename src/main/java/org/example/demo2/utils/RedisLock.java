package org.example.demo2.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisLock implements Lock{


    private String lockName;
    private StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "lock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString().replace("-", "");
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    static{
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua")); // classpath 就是resources
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    public RedisLock(StringRedisTemplate redisTemplate, String lockName) {
        this.redisTemplate = redisTemplate;
        this.lockName = lockName;
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) {
        // 获取线程名称
        String ThreadId = ID_PREFIX + Thread.currentThread().getId();

        // 如果不存在，说明锁获取成功，即没人用这个锁
        Boolean lockStatus = redisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + lockName, ThreadId, timeout, TimeUnit.SECONDS);

        // 解决自动拆箱可能空指针问题
        return Boolean.TRUE.equals(lockStatus);
    }

    @Override
    public void unlock() {
        redisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(KEY_PREFIX + lockName),
                ID_PREFIX + Thread.currentThread().getId()); //使用 lua保证原子性
    }


//    @Override
//    public void unlock() {
//        // 判断是否锁为当前线程的锁，如果不是则不会触发误释放锁，进而导致其他线程获取成功
//        String threadID = ID_PREFIX + Thread.currentThread().getId();
//        String id = redisTemplate.opsForValue().get(KEY_PREFIX + lockName);
//        if(threadID.equals(id)){
//            redisTemplate.delete(KEY_PREFIX + this.lockName);
//        }
//    }
}
