package com.example.homework.demo.Config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {
	
	@Value("${spring.redis.port}")
	private int redisPort;
	
	private RedisServer redisServer;
	
	@PostConstruct //객체의 초기화 was가 띄워질때 실행됨.
	public void redisServer() {
		redisServer = new RedisServer(redisPort);
		
		redisServer.start();
	}
	
	@PreDestroy // 객체를 제거하기전에 해야할 작업이 있을경우 쓰는 어노테이션
	public void stopRedis() {
		if(redisServer != null) {
			redisServer.stop();
		}
	}

}
