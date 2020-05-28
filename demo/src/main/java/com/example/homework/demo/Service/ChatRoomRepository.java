package com.example.homework.demo.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.homework.demo.WebSocketDTO.ChatRoom;
import com.example.homework.demo.pubsub.RedisSubscriber;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//@Repository
@Service
public class ChatRoomRepository {
	
	/*
	 * topic 신규 생성하고 redisMessasgeListner연동하던거 필요 없어졌으므로 다 주석처리
	 */
	//topic(채팅방)에 발행될 메시지를 처리할 리스너
	//private final RedisMessageListenerContainer redisMessageListener;
	
	//구독 처리
	//private final RedisSubscriber redisSubscriber;
	
	//redis CacheKeys
	private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
	private static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트 수
	private static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 Id를 매핑한 정보 저장
	//private final RedisTemplate<String, Object> redisTemplate;
	
	//채팅방 정보가 초기화되지 않도로고 redisHash에 저장하도록 처리
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ChatRoom> opsHashChatRoom;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> hashOpsEnterInfo;
	
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;
	//채팅방의 대화 메시지를 발행하기 위한 topic 정보들. 서버별로 채팅방에 매칭되는 topic정보를 map에 넣어서 roomId로 찾을 수 있도록 한다.
	//private Map<String, ChannelTopic> topics; 
	
//	@PostConstruct
//	private void init() {
//		opsHashChatRoom = redisTemplate.opsForHash();
//		//topics = new HashMap<>();
//	}
	
	public List<ChatRoom> findAllRoom(){
		return opsHashChatRoom.values(CHAT_ROOMS);
	}
	
	public ChatRoom findRoomById(String id) {
		return opsHashChatRoom.get(CHAT_ROOMS, id);
	}
	
	//채팅방 생성 : 서버간 채팅방 공유를 위해 redis Hash에 저장
	public ChatRoom createChatRoom(String name) {
		ChatRoom chatRoom = ChatRoom.create(name);
		opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(),chatRoom);
		return chatRoom;
	}
	
	// 유저가 입장한 채팅방 id와 유저 세션id 매핑 정보 저장
	public void setUserEnterInfo(String sessionId, String roomId) {
		hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
	}
	
	// 유저 세션으로 입장한 채팅방 id조회
	public String getUserEnterRoomId(String sessionId) {
		return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
	}
	
	// 유저 세션 정보와 맵핑된 채팅방 id 삭제
	public void removeUserEnterInfo(String sessionId) {
		hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
	}
	
	// 채팅방 유저수 조회
	public long getUserCount(String roomId) {
		return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
	}
	
	// 채팅방 유저수 +1
	public long plusUserCount(String roomId) {
		return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
	}
	
	public long minusUserCount(String roomId) {
		return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
	}
	//채팅방 입장 : Redis에 topic을 만들고 pub/sub 통신을 위해 리스너 설정
//	public void enterChatRoom(String roomId) {
//		ChannelTopic topic = topics.get(roomId);	
//		if(topic == null) {
//			topic = new ChannelTopic(roomId);
//			redisMessageListener.addMessageListener(redisSubscriber, topic);
//			topics.put(roomId,topic);
//		}
//	}
//	
//	public ChannelTopic getTopic(String roomId) {
//		return topics.get(roomId);
//	}
}

//package com.example.homework.demo.Service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.stereotype.Repository;
//
//import com.example.homework.demo.WebSocketDTO.ChatRoom;
//
//@Repository
//public class ChatRoomRepository {
//	
//	private Map<String,ChatRoom> chatRoomMap;
//	
//	@PostConstruct
//	private void init() {
//		chatRoomMap = new LinkedHashMap<String, ChatRoom>();
//	}
//	
//	public List<ChatRoom> findAllRoom(){
//		List<ChatRoom> chatRooms = new ArrayList<>(chatRoomMap.values());
//		Collections.reverse(chatRooms);
//		return chatRooms;
//	}
//	
//	public ChatRoom findRoomById(String id) {
//		return chatRoomMap.get(id);
//	}
//	
//	public ChatRoom createChatRoom(String name) {
//		ChatRoom chatRoom = ChatRoom.create(name);
//		chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
//		return chatRoom;
//	}
//}

