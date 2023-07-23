package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.chat.ChatDTO;
import com.pradip.roommanagementsystem.entity.chat.Chat;
import com.pradip.roommanagementsystem.repository.ChatMessageRepository;
import com.pradip.roommanagementsystem.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private GeneralUtil generalUtil;

    public Object getAllChatMessages() {
        return null;
    }

    public Object getAllChats(String token) {
        return null;
    }

    public Object getChatById(Long chatId) {
        return null;
    }

    public Chat saveChat(ChatDTO chat) {
        return null;
    }

    public Object deleteChat(Long chatId) {
        return null;
    }
}
