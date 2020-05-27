package com.example.homework.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.example.homework.demo.handler.StompHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이나 @NonNull인 필드 값만 파라미터로 받는 생성자를 만들어줌
@Configuration //어노테이션 기반 환경 구성
//@EnableWebSocket //웹소켓 활성화
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer{
	//public class WebSockConfig implements WebSocketConfigurer{
	//private final WebSocketHandler webSocketHandler;
	
	private final StompHandler stompHandler;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/sub"); //메시지를 구독 요청 prefix 설정
		config.setApplicationDestinationPrefixes("/pub"); // 메시지를 발행하는 요청 prefix 설정
	}
	
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
	//public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		//registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
		//										endpoint	도메인이 다른 서버에서도 접속 가능하도록 설정 추가
		
		registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();
		
		
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}

}
