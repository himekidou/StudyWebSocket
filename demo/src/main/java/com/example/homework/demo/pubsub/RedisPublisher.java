package com.example.homework.demo.pubsub;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.example.homework.demo.WebSocketDTO.ChatMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
	
	private final RedisTemplate<String, Object> redisTemplate;
	
	//채팅방에 입장시 메시지를 작성하면 해당 메시지를 Redis Topic에 발행함
	//발행시 대기하고 있던 redis구독 서비스가 메시지를 처리함
	public void publish(ChannelTopic topic, ChatMessage message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}

}


/*
 * redis pub sub 특징
 * 
 * 지속적이지 않다. => Subscriber에 직접 전달후 삭제, 메모리 또는 디스크에 기록 안남음
 * Subscriber가 메시지를 받는것을 보장하지 않음 => 안정적이지 않으나 처리가 무척 빠름
 */
