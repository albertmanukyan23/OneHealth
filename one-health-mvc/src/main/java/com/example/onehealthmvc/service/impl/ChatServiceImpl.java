package com.example.onehealthmvc.service.impl;


import com.example.onehealthcommon.entity.ChatMessage;
import com.example.onehealthcommon.repository.ChatRepository;
import com.example.onehealthmvc.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        chatRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findAllMessages() {
        return chatRepository.findAll();
    }


}
