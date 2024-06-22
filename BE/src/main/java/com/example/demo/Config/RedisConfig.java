package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

public class RedisConfig {

    private static Jedis jedis;

    public synchronized static Jedis getRedisInstance() {
        if (jedis == null) {
            jedis = new Jedis("redis://default:xcHV2iYQow6KxZMxZz4klIoKxgYuytAf@redis-17341.c251.east-us-mz.azure.redns.redis-cloud.com:17341");
        }
        return jedis;
    }
}
