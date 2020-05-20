package com.example.homework.demo.Controller;

//import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

import com.example.homework.demo.WebSocketDTO.ChatMessage;
//import com.example.homework.demo.Service.ChatService;
//import com.example.homework.demo.WebSocketDTO.ChatRoom;

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
	
	private final SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/chat/message") //websocket으로 들어오는 메시지 발행 처리
	public void message(ChatMessage message) {
		//클라에서 prefix를 붙여서 /pub/chat/message로 발행요청 -> Controller가 요청처리
		if(ChatMessage.MessageType.ENTER.equals(message.getType())) {
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		}
		
		//해당 주소를 구독하고 있다가 메시지가 전달되면 화면에 출력.
		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(),message);
		
	}
	
	
}
