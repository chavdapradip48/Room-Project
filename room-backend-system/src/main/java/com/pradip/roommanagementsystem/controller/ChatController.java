package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.chat.ChatDTO;
import com.pradip.roommanagementsystem.entity.chat.Chat;
import com.pradip.roommanagementsystem.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
@Slf4j
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getChats() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Chats fetched successfully.", chatService.getAllChats(""))
        );
    }

    @GetMapping("/session-user")
    public ResponseEntity<ApiResponse<Object>> getSessionUserChatChats(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Chats fetched successfully.", chatService.getAllChats(token))
        );
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Object> getChatById(@PathVariable Long chatId) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Chats fetched successfully.", chatService.getChatById(chatId))
        );
    }

    @PostMapping
    public ResponseEntity<Object> addChat(@Valid @RequestBody ChatDTO chat){
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value(),
                "Chat saved successfully.", chatService.saveChat(chat))
        );
    }

    @PostMapping("/by-group")
    public ResponseEntity<Object> addGroupChat(@Valid @RequestBody ChatDTO chat){
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value(),
                "Chat saved successfully.", chatService.saveChat(chat))
        );
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Object> deleteChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value()
                , "Chat deleted successfully.",chatService.deleteChat(chatId))
        );
    }
}


