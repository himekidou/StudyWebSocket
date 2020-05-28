package com.example.homework.demo.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.example.homework.demo.WebSocketDTO.ChatMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService{
	
	private final ChannelTopic channelTopic;
	private final RedisTemplate redisTemplate;
	private final ChatRoomRepository chatRoomRepository;
	
	//destination에서 roomId 가져오기
	public String getRoomId(String destination) {
		int lastIndex = destination.lastIndexOf('/');
		if(lastIndex != -1) {
			return destination.substring(lastIndex + 1);
		}
		else {
			return "";
		}
	}
	
	// 채팅방에 메시지 발송
	public void sendChatMessage(ChatMessage chatMessage) {
		chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));
		if(ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
			chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장하셨습니다.");
			chatMessage.setSender("[알림]");
		}
		else if(ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
			chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나가셨습니다.");
			chatMessage.setSender("[알림]");
		}
		
		redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
	}
}


















//	package com.example.homework.demo.Service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//import com.example.homework.demo.WebSocketDTO.ChatRoom;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class ChatService {
//	
//	private final ObjectMapper objectMapper;
//	private Map<String, ChatRoom> chatRooms; //서버에 생성된 모든 채팅방의 정보 저장
//	
//	@PostConstruct
//	private void init() {
//		chatRooms = new LinkedHashMap<>();
//	}
//	
//	public List<ChatRoom> findAllRoom(){
//		return new ArrayList<>(chatRooms.values());
//	}
//	
//	public ChatRoom findRoomById(String roomId) {
//		return chatRooms.get(roomId);
//	}
//	
//	public ChatRoom createRoom(String name) {
//		String randomId = UUID.randomUUID().toString(); //random uuid로 구별id를 가진 객체 생성 채팅방 map에 추가
//		ChatRoom chatRoom = ChatRoom.builder()
//				.roomId(randomId)
//				.name(name)
//				.build();
//		chatRooms.put(randomId,chatRoom);
//		return chatRoom;
//	}
//	
//	//지정한 web session에 메시지를 발송
//	public <T> void sendMessage(WebSocketSession session, T message) {
//		try {
//			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
//		}catch(IOException e) {
//			log.error(e.getMessage(),e);
//		}
//	}
//}
