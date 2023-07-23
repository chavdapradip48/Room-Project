package com.pradip.roommanagementsystem.dto.chat;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pradip.roommanagementsystem.dto.ChatUser;
import com.pradip.roommanagementsystem.entity.chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGroupDTO {
    private Long id;

    private String name;

    @JsonProperty("members")
    private Set<ChatUser> members;

//    private Chat chat;
}
