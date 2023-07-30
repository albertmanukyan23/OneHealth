package com.example.onehealthcommon.repository;

import com.example.onehealthcommon.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatMessage, Integer> {


}