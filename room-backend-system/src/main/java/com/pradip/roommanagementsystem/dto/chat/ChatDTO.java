package com.pradip.roommanagementsystem.dto.chat;

import com.pradip.roommanagementsystem.dto.ChatUser;
import com.pradip.roommanagementsystem.dto.UserDTO;
import lombok.Data;

@Data
public class ChatDTO {

    private Long id;

    private boolean isGroupChat;

    private ChatUser sender;

    private ChatUser receiver;

    private ChatGroupDTO chatGroup;

}
