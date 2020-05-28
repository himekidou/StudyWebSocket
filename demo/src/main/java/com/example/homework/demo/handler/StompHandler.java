package com.example.homework.demo.handler;

import java.security.Principal;
import java.util.Optional;

import org.apache.tomcat.util.net.Acceptor.AcceptorState;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.example.homework.demo.Service.ChatRoomRepository;
import com.example.homework.demo.Service.ChatService;
import com.example.homework.demo.Service.JwtTokenProvider;
import com.example.homework.demo.WebSocketDTO.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//WebSocket연결시 요청 해더의 토큰 유효성 검증하는 코드

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor{
	
	private final JwtTokenProvider jwtTokenProvider;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatService chatService;
	
	//WebSocket을 통해 들어온 요청이 처리되기 전에 실행
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel){
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		
		// WebSocket 연결시 헤더의 토큰 검증
		if(StompCommand.CONNECT == accessor.getCommand()) { // WebSocket 연결
			//jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("token"));
			String jwtToken = accessor.getFirstNativeHeader("token");
			jwtTokenProvider.validateToken(jwtToken);
		}
		else if(StompCommand.SUBSCRIBE == accessor.getCommand()) { //채팅룸 구독 요청
			//헤더에서 구독 destination 정보 얻어오고, roomId 추출
			String roomId = chatService.getRoomId(Optional.ofNullable((String)message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
			
			// 채팅방에 들어온 클라 sessionId를 roomId와 매핑해 둔다. 특정세션을 확인하기 위해서.
			String sessionId = (String) message.getHeaders().get("simpSessionId");
			chatRoomRepository.setUserEnterInfo(sessionId, roomId);
			// 채팅방의 인원수를 +1 한다
			chatRoomRepository.plusUserCount(roomId);
			//클라이언트 입장 메시지 발송
			String name = Optional.ofNullable((Principal)message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
			
			chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
			log.info("SUBSCRIBED {}, {}", name,roomId);
		}
		else if(StompCommand.DISCONNECT == accessor.getCommand()) { //WebSocket 연결 종료
			//연결 종료된 클라 sessionId로 roomId 추출
			String sessionId = (String) message.getHeaders().get("simpSessionId");
			String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
			
			//채팅방의 인원수 -1
			chatRoomRepository.minusUserCount(roomId);
			
			// 클라이언트 퇴장 메시지 발송
			String name = Optional.ofNullable((Principal)message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
			chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(name).build());
			
			//퇴장한 클라이언트의 roomId 매핑 정보 제거
			chatRoomRepository.removeUserEnterInfo(sessionId);
			log.info("DISCONNECTED {}, {}",sessionId,roomId);
			
		}
		
		return message;
	}

}
