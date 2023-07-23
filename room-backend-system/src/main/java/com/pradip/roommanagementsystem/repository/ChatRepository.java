package com.pradip.roommanagementsystem.repository;

import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findBySenderId(Long senderId);
}
