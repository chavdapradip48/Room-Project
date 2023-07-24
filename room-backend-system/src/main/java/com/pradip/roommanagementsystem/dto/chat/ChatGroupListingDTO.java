package com.pradip.roommanagementsystem.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pradip.roommanagementsystem.dto.ChatUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
public class ChatGroupListingDTO {
    private Long id;

    private String name;
}
