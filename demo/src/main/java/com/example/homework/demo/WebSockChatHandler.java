package com.example.homework.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.homework.demo.Service.ChatService;
import com.example.homework.demo.WebSocketDTO.ChatMessage;
import com.example.homework.demo.WebSocketDTO.ChatRoom;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSockChatHandler extends TextWebSocketHandler {
//socket 통신은 서버와 클라이언트가 1:N으로 관계를 맺기 때문에 한서버에 여러 클라이언트가 접속하고 여러 클라이언트가 발송한 메세지를 받아 처리해줄 handler
	
	private final ObjectMapper objectMapper;
	private final ChatService chatService;
	
	@Override
	protected void handleTextMessage(WebSocketSession session , TextMessage message) throws Exception {
		
		String payload = message.getPayload(); //메시지 데이터
		log.info("payload {}",payload);
		
		//TextMessage textMessage = new TextMessage("Welcome");
		
		//session.sendMessage(textMessage);
		
		//웹소켓 클라이언트로부터 메시지 전달 받아서 객체로 변환
		ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
		
		//전달받은 메시지에 담긴 채팅방 id로 발송 대상 체팅방 정보 조회
		ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
		
		//해당 채팅방에 입장해있는 모든 클라이언트들에게 타입에 따른 메시지 발송
		room.handleActions(session, chatMessage, chatService);
		
	}
}
