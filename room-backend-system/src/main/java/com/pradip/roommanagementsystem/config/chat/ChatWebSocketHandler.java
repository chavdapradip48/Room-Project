package com.pradip.roommanagementsystem.config.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pradip.roommanagementsystem.entity.chat.Chat;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.repository.UserRepository;
import com.pradip.roommanagementsystem.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;
    private final ChatService chatService;

    @Autowired
    public ChatWebSocketHandler(SimpMessageSendingOperations messagingTemplate, UserRepository userRepository,
                                ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = session.getPrincipal().getName();
        User user = userRepository.findByEmail(username).get();
        user.setOnlineStatus(true);
        userRepository.save(user);
        broadcastOnlineStatusUpdate(user);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = session.getPrincipal().getName();
        User user = userRepository.findByEmail(username).get();
        user.setOnlineStatus(false);
        userRepository.save(user);
        broadcastOnlineStatusUpdate(user);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Chat chat = new ObjectMapper().readValue(payload, Chat.class);
//        chat.setSender(userRepository.findByEmail(session.getPrincipal().getName()).get());

        // Save the message to the database
//        chatService.saveChat(chat);

        // Send the message to the chat participants (assuming you have a method to find the chat participants' WebSocket sessions)
//        Set<User> chatParticipants = chat.getParticipants();
//        for (User participant : chatParticipants) {
//            WebSocketSession participantSession = findParticipantSession(participant.getEmail());
//            if (participantSession != null && participantSession.isOpen()) {
//                participantSession.sendMessage(message);
//            }
//        }
    }

    private WebSocketSession findParticipantSession(String participantUsername) {
        // Implement this method to find the WebSocketSession of the chat participant based on the participant's username.
        // You might store WebSocket sessions in a map or use a user-specific cache for efficient retrieval.
        // If the participant is not connected, return null or handle the situation accordingly.
        return null;
    }

    private void broadcastOnlineStatusUpdate(User user) {
        messagingTemplate.convertAndSend("/topic/online-status", user);
    }
}
