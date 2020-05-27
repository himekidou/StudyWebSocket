package com.example.homework.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.homework.demo.pubsub.RedisSubscriber;

@Configuration
public class RedisConfig {
	
	//단일 topic으로 쓰기 위해서 빈 추가
	@Bean
	public ChannelTopic channelTopic() {
		return new ChannelTopic("chatroom");
	}
	
	/*
	 * @Bean // redis pub/sub 메시지 처리하는 리스너 설정
	 *  public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
	 * RedisMessageListenerContainer container = new
	 * RedisMessageListenerContainer();
	 * 
	 * container.setConnectionFactory(connectionFactory); return container; }
	 */
	
	//메시지 리스너 단일화
	@Bean
	public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter, ChannelTopic channelTopic) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, channelTopic);
		return container;
	}
	
	//실제 메시지를 처리하는 subscriber 설정 추가 => 구독자에게 보내는 역할
	
	@Bean
	public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
		return new MessageListenerAdapter(subscriber,"sendMessage");
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
