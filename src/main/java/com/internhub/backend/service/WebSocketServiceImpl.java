package com.internhub.backend.service;

import com.internhub.backend.dto.TextMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate template;


    @Override
    public void sendPrivateMessage(String userId, String messageContent) {
        TextMessageDTO textMessageDTO = new TextMessageDTO();
        textMessageDTO.setMessage("Hồ sơ doanh nghiệp của bạn đã được duyệt");
        template.convertAndSendToUser(
                userId,
                "/private",
                textMessageDTO
        );
    }
}
