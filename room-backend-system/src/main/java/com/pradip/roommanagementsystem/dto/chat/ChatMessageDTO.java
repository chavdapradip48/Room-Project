package com.pradip.roommanagementsystem.dto.chat;

import com.pradip.roommanagementsystem.enumm.MessageType;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatMessageDTO {

    private Long id;

    private MessageType type;

    private String content;

    private Timestamp timestamp;

}
