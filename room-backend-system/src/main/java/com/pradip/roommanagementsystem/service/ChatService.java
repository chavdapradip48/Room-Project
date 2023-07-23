package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.ChatUser;
import com.pradip.roommanagementsystem.dto.chat.ChatDTO;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.entity.chat.Chat;
import com.pradip.roommanagementsystem.exception.ChatException;
import com.pradip.roommanagementsystem.repository.ChatRepository;
import com.pradip.roommanagementsystem.security.util.JwtUtils;
import com.pradip.roommanagementsystem.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private GeneralUtil generalUtil;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    public Chat saveChat(ChatDTO chatMessage) {

        boolean isreqReceiverExist = userService.isUserExistUserById(chatMessage.getReceiver().getId());
        boolean isreqSenReceiverderExist = userService.isUserExistUserById(chatMessage.getSender().getId());

        if (!isreqReceiverExist || !isreqSenReceiverderExist)
            throw new ChatException("Sender or Receiver might not exist with us");

        return chatRepository.save(ChatDTOToEntity(chatMessage));
    }

    public Object getAllChats(String token) {

        List<Chat> chats;
        if (!token.isEmpty()) chats = chatRepository.findBySenderId(Long.parseLong(jwtUtils.getAllClaimsFromToken(jwtUtils.removeBearer(token)).get("id").toString()));
        else chats = chatRepository.findAll();

        if (chats != null && chats.isEmpty())
            throw new ChatException("Chats does not available");

        return chats.stream().map(chat -> ChatEntityToDTO(chat)).collect(Collectors.toList());
    }

    private ChatDTO ChatEntityToDTO(Chat chat) {
        ChatDTO chatDTO = generalUtil.convertObject(chat, ChatDTO.class);

        chatDTO.setSender(generalUtil.convertObject(chat.getSender(), ChatUser.class));
        chatDTO.setReceiver(generalUtil.convertObject(chat.getReceiver(), ChatUser.class));

        return chatDTO;
    }

    private Chat ChatDTOToEntity(ChatDTO chatDTO) {
        Chat chat = new Chat();
        chat.setGroupChat(chatDTO.isGroupChat());
        chat.setSender(generalUtil.convertObject(chatDTO.getSender(), User.class));
        chat.setReceiver(generalUtil.convertObject(chatDTO.getReceiver(), User.class));

        return chat;
    }

    public ChatDTO getChatById(Long chatId) {

        Optional<Chat> chatById = chatRepository.findById(chatId);
        if (chatById.isEmpty())
            throw new ChatException("Chat is not available with "+chatId+" Id");

        return ChatEntityToDTO(chatById.get());
    }

    public ChatDTO deleteChat(Long chatId) {

        ChatDTO chatById = getChatById(chatId);
        chatRepository.deleteById(chatId);

        return chatById;
    }
}
