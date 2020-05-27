package com.example.homework.demo.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.example.homework.demo.Service.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//WebSocket연결시 요청 해더의 토큰 유효성 검증하는 코드

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor{
	
	private final JwtTokenProvider jwtTokenProvider;
	
	//WebSocket을 통해 들어온 요청이 처리되기 전에 실행
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel){
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		
		// WebSocket 연결시 헤더의 토큰 검증
		if(StompCommand.CONNECT == accessor.getCommand()) {
			jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("token"));
		}
		
		return message;
	}

}
