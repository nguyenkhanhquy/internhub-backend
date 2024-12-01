package com.internhub.backend.service;

public interface WebSocketService {

    void sendPrivateMessage(String userId, String messageContent);
}
