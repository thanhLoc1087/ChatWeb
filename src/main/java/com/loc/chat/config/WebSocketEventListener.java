package com.loc.chat.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.loc.chat.chat.ChatMessage;
import com.loc.chat.chat.ChatMessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;
    
    @EventListener
    public void handleWebSocketDisconnectListener(
        SessionDisconnectEvent event
    ) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            var chatMessage = ChatMessage.builder()
                .type(ChatMessageType.LEAVE)
                .sender(username)
                .build();
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
