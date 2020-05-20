package com.example.homework.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이나 @NonNull인 필드 값만 파라미터로 받는 생성자를 만들어줌
@Configuration //어노테이션 기반 환경 구성
@EnableWebSocket //웹소켓 활성화
public class WebSockConfig implements WebSocketConfigurer{
	private final WebSocketHandler webSocketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
		//										endpoint	도메인이 다른 서버에서도 접속 가능하도록 설정 추가
		
		
	}

}
