package com.pradip.roommanagementsystem.entity.chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pradip.roommanagementsystem.enumm.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "chat_messages")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"chat"})
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private MessageType type;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Timestamp timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    @JsonBackReference
    private Chat chat;

}
