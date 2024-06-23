package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;

@SpringBootApplication
public class Sep490BeApplication {

	public static void main(String[] args) {
//		Jedis jedis = new Jedis("redis://default:xcHV2iYQow6KxZMxZz4klIoKxgYuytAf@redis-17341.c251.east-us-mz.azure.redns.redis-cloud.com:17341");
//		jedis.setex("minh",60,"leaddzvl");
//		System.out.println(jedis.get("minh"));
		SpringApplication.run(Sep490BeApplication.class, args);
	}

}
