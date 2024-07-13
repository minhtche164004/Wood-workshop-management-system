package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.*;

@Configuration
public class RedisConfig {


    private volatile static JedisPooled redisInstance;
    public synchronized static JedisPooled getRedisInstance() {
        if(redisInstance != null){
            return redisInstance;
        }
        else {
            HostAndPort address = new HostAndPort("redis-17341.c251.east-us-mz.azure.redns.redis-cloud.com", 17341);
            JedisClientConfig config = DefaultJedisClientConfig.builder()
                    .user("default")
                    .password("xcHV2iYQow6KxZMxZz4klIoKxgYuytAf")
                    .build();
            redisInstance = new JedisPooled(address, config);
        }
        return redisInstance;
    }

    public static void main(String[] args) {

    }
}
