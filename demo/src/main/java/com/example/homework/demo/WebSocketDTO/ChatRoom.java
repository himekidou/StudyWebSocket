package com.example.homework.demo.WebSocketDTO;

//import java.util.HashSet;
//import java.util.Set;
import java.util.UUID;

//import org.springframework.web.socket.WebSocketSession;

//import com.example.homework.demo.Service.ChatService;

//import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoom {
	private String roomId; //채팅방 아이디
	private String name;   //채팅방 이름
	
	public static ChatRoom create(String name) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.roomId = UUID.randomUUID().toString();
		chatRoom.name = name;
		return chatRoom;
	}

	
	
	
	//private Set<WebSocketSession> sessions = new HashSet<>(); 
	//입장한 클라이언트들의 정보를 가지고 있어야함으로 session으로 가지고 있음	
//구독자 관리가 알아서 되므로 웹소켓 세션 관리가 필요가 없어짐.	
//	@Builder //이펙티브 자바 스타일과 비슷한 빌더 패턴 코드가 빌드됨
//	public ChatRoom(String roomId, String name) {
//		this.roomId = roomId;
//		this.name = name;
//	}
//	
//	//입장시와 대화시의 분기처리
//	public void handleActions(WebSocketSession session, 
//			ChatMessage chatMessage, ChatService chatService) {
//		
//		if(chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
//			sessions.add(session);
//			chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
//		}
//			
//		sendMessage(chatMessage, chatService);
//		
//	}
//	
//	public <T> void sendMessage(T message, ChatService chatService) {
//		sessions.parallelStream().forEach(session ->
//			chatService.sendMessage(session,message)
//				);
//	}

	
	

}
