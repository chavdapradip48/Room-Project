package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.ChatUser;
import com.pradip.roommanagementsystem.dto.chat.ChatDTO;
import com.pradip.roommanagementsystem.dto.chat.ChatGroupDTO;
import com.pradip.roommanagementsystem.dto.chat.ChatGroupListingDTO;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.entity.chat.Chat;
import com.pradip.roommanagementsystem.entity.chat.ChatGroup;
import com.pradip.roommanagementsystem.exception.ChatException;
import com.pradip.roommanagementsystem.repository.ChatGroupRepository;
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

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    public ChatDTO saveChat(ChatDTO chatMessage) {

        boolean isreqSenderExist = userService.isUserExistUserById(chatMessage.getSender().getId());
        boolean isChatExist;

        if (chatMessage.isGroupChat()) {
            isChatExist = chatRepository.existsBySenderIdAndChatGroupId(chatMessage.getSender().getId(), chatMessage.getChatGroup().getId());
            ChatGroupListingDTO chatGroup = chatMessage.getChatGroup();

            if (!isreqSenderExist || !chatGroupRepository.existsById(chatGroup.getId()))
                throw new ChatException("Sender or Group might not exist");

        } else {
            isChatExist = chatRepository.existsBySenderIdAndReceiverId(chatMessage.getSender().getId(), chatMessage.getReceiver().getId());
            boolean isreqSenReceiverderExist = userService.isUserExistUserById(chatMessage.getSender().getId());

            if (!isreqSenderExist || !isreqSenReceiverderExist)
                throw new ChatException("Sender or Receiver might not exist");
        }

        if (isChatExist)
            throw new ChatException("Chats have already been established");

        Chat chat = ChatDTOToEntity(chatMessage);
        return ChatEntityToDTO(chatRepository.save(chat));
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

        if(!chat.isGroupChat()) chatDTO.setReceiver(generalUtil.convertObject(chat.getReceiver(), ChatUser.class));
        else chatDTO.setChatGroup(generalUtil.convertObject(chat.getChatGroup(), ChatGroupListingDTO.class));

        return chatDTO;
    }

    private Chat ChatDTOToEntity(ChatDTO chatDTO) {

        Chat chat = new Chat();
        chat.setGroupChat(chatDTO.isGroupChat());

        if(!chat.isGroupChat()){
            chat.setReceiver(generalUtil.convertObject(chatDTO.getReceiver(), User.class));
        }
        else {
            Optional<ChatGroup> byId = chatGroupRepository.findById(chatDTO.getChatGroup().getId());
            chat.setChatGroup(byId.get());
        }
        chat.setSender(generalUtil.convertObject(chatDTO.getSender(), User.class));

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
