package com.example.homework.demo.WebSocketDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
	
	//메시지 타입 : 입장했을때, 채팅쳤을때
	public enum MessageType{
		ENTER, TALK
	}
	
	private MessageType type;	//메시지 타입
	private String roomId; 		//방번호 
	private String sender;		//메시지 보낸 사람
	private String message;		//메시지

}
