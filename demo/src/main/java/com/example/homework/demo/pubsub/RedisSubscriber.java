package com.example.homework.demo.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.homework.demo.WebSocketDTO.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener{
	
	private final ObjectMapper objectMapper;
	private final RedisTemplate redisTemplate;
	private final SimpMessageSendingOperations messagingTemplate;
	
	//Redis에서 메시지가 발행되면 대기하고 있던 onMessage가 메시지 처리함
	@Override
	public void onMessage(Message message, byte[] pattern) { // 두번째 파라미터 : 채널 메칭을 위한 다양한 패턴 처리 
		try {
			// redis에서 발행된 데이터를 받아서 직렬화
			String publishMessage = (String)
		redisTemplate.getStringSerializer().deserialize(message.getBody());
			
			//ChatMessage객체로 매핑
			ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
			
			//WebSocket 구독자에게 채팅 메시지를 보냄
			messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), roomMessage);
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
