package com.example.onehealthmvc.service;

import com.example.onehealthcommon.entity.ChatMessage;

import java.util.List;

public interface ChatService {
    public void saveMessage(ChatMessage chatMessage);

    List<ChatMessage> findAllMessages();

}
