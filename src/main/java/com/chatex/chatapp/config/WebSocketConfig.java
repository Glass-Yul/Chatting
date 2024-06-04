package com.chatex.chatapp.config;

import com.chatex.chatapp.handler.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private SocketHandler socketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 웹소켓 서버 핸들러 => js에 연결됨
        registry.addHandler(socketHandler, "/chating");

        // .setAllowedOrigins("*"); // 모든 출처에서의 요청 허용
        // 위 메소드를 작성해주면 /chating 안으로 들어오는 모든 출처의 요청을 허용함
    }
}
