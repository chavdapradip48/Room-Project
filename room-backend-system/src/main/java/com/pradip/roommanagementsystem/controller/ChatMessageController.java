package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.chat.ChatDTO;
import com.pradip.roommanagementsystem.entity.chat.Chat;
import com.pradip.roommanagementsystem.service.ChatMessageService;
import com.pradip.roommanagementsystem.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/chat/message")
@CrossOrigin(origins = "*")
@Slf4j
public class ChatMessageController {
    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getChats() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Chats fetched successfully.", chatMessageService.getAllChatMessages())
        );
    }

    @GetMapping("/session-user")
    public ResponseEntity<ApiResponse<Object>> getSessionUserChatChats(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Chats fetched successfully.", chatMessageService.getAllChats(token))
        );
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Object> getChatById(@PathVariable Long chatId) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Chats fetched successfully.", chatMessageService.getChatById(chatId))
        );
    }

    @PostMapping
    public ResponseEntity<Object> addChat(@Valid @RequestBody ChatDTO chat){
        return ResponseEntity.ok(new ApiResponse<Chat>(HttpStatus.OK.value(),
                "Chat saved successfully.", chatMessageService.saveChat(chat))
        );
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Object> deleteChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value()
                , "Chat deleted successfully.",chatMessageService.deleteChat(chatId))
        );
    }
}


