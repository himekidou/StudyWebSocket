package com.example.homework.demo.WebSocketDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
	
	public ChatMessage() {
		
	}
	
	@Builder
	public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount) {
		this.type = type;
		this.roomId = roomId;
		this.sender = sender;
		this.message = message;
		this.userCount = userCount;
	}
	
	//메시지 타입 : 입장했을때, 채팅쳤을때
	public enum MessageType{
		ENTER, TALK, QUIT
	}
	
	private MessageType type;	//메시지 타입
	private String roomId; 		//방번호 
	private String sender;		//메시지 보낸 사람
	private String message;		//메시지
	private long userCount;		// 채팅방 인원수, 채팅방 내에서 메시지가 전달될 때 인원수 갱신시 변경
	
}
