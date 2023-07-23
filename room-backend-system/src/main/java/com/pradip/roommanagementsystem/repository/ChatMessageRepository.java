package com.pradip.roommanagementsystem.repository;

import com.pradip.roommanagementsystem.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}