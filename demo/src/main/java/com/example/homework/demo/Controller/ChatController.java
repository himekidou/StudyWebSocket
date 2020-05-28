package com.example.homework.demo.Controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;

//import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

import com.example.homework.demo.Service.ChatRoomRepository;
import com.example.homework.demo.Service.JwtTokenProvider;
import com.example.homework.demo.WebSocketDTO.ChatMessage;
import com.example.homework.demo.Service.ChatService;
//import com.example.homework.demo.WebSocketDTO.ChatRoom;
//import com.example.homework.demo.pubsub.RedisPublisher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//@RestController
//@RequestMapping("/chat")
@Controller
public class ChatController {
	
	/*
	 * private final ChatService chatService;
	 * 
	 * @PostMapping public ChatRoom createRoom(@RequestParam String name) { return
	 * chatService.createRoom(name); }
	 * 
	 * @GetMapping public List<ChatRoom> findAllRoom(){ return
	 * chatService.findAllRoom(); }
	 */
	
	//직접 template처리하지말고 redis의 topic으로 발행해서 다른서버에 공유함
	//private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final ChatService chatService;
	
	//private final RedisTemplate redisTemplate;
	//public final ChannelTopic channelTopic;
	
	//private final SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/chat/message") //websocket으로 들어오는 메시지 발행 처리 "/pub/chat/message"
	public void message(ChatMessage message, @Header("token") String token) { // 서버에 메시지 전달할 때도 유효성 검사 해야하므로 대화명 조회해서 체크함
		String nickname = jwtTokenProvider.getUserNameFormJwt(token);
		
		//로그인 회원 정보로 대화명 설정
		message.setSender(nickname);
		
		//클라에서 prefix를 붙여서 /pub/chat/message로 발행요청 -> Controller가 요청처리
		//서버에서 메시지 보내게 처리했으므로 컨트롤러는 그외의 대화 메시지만 처리
		/*
		 * if(ChatMessage.MessageType.ENTER.equals(message.getType())) {
		 * //chatRoomRepository.enterChatRoom(message.getRoomId());
		 * message.setSender("[알림]"); message.setMessage(nickname + "님이 입장하셨습니다."); }
		 */
		
		// 채팅방 인원수 세팅
		message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
		
		//WebSocket에 발행된 메시지 redis로 발행(publish)
		chatService.sendChatMessage(message);
		
		//해당 주소를 구독하고 있다가 메시지가 전달되면 화면에 출력.
		//messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(),message);
		
		// WebSocket에 발행된 메시지를 redis로 발행한다(publish)
		//redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
		//redisTemplate.convertAndSend(channelTopic.getTopic(), message);
	}
	
	
}
