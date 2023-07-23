package com.pradip.roommanagementsystem.repository;

import com.pradip.roommanagementsystem.entity.chat.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ChatGroupRepository extends JpaRepository<ChatGroup,Long> {
}
