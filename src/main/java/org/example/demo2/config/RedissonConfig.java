package org.example.demo2.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        // 配置
        Config config = new Config();
        // config.useSingleServer().setAddress("redis://localhost:6379");
        config.useClusterServers()
                .addNodeAddress("redis://127.0.0.1:6380", "redis://127.0.0.1:6381", "redis://127.0.0.1:6382")
                .setScanInterval(5000)
                .setReadMode(ReadMode.SLAVE);
        return Redisson.create(config);
    }
}
