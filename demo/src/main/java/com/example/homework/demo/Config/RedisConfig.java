package com.example.homework.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	
	@Bean // redis pub/sub 메시지 처리하는 리스너 설정
	public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		
		container.setConnectionFactory(connectionFactory);
		return container;
	}
	
	@Bean // 어플리케이션에서 사용할 redisTemplate 설정
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키값 직렬화 역직렬화를 위해 설정
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); // json으로 받을 value값을 휘애 직렬화 역질렬화 해줌.
		
		return redisTemplate;
	}
}
