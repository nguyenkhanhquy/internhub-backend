package com.internhub.backend.controller;

import com.internhub.backend.dto.TextMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ws-demo")
public class WebSocketTextController {

    private final SimpMessagingTemplate template;

    @Autowired
    public WebSocketTextController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody TextMessageDTO textMessageDTO) {
        template.convertAndSend("/topic/message", textMessageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send-to-user")
    public ResponseEntity<Void> sendPrivateMessage(@RequestBody TextMessageDTO textMessageDTO,
                                                   @RequestParam String id) {
        // Gửi tới endpoint /user/{id}/private
        template.convertAndSendToUser(
                id,
                "/private",
                textMessageDTO
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
