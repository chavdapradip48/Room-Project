package com.pradip.roommanagementsystem.entity.chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pradip.roommanagementsystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"sender", "receiver", "chatMessages", "chatGroup"})
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isGroupChat;

    @OneToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonBackReference
    private User sender;

    @OneToOne
    @JoinColumn(name = "receiver_id")
    @JsonBackReference
    private User receiver;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private ChatGroup chatGroup;
}
