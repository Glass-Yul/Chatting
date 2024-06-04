package com.chatex.chatapp.handler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

@Component
public class SocketHandler extends TextWebSocketHandler { // 구현체 등록

    // 웹소켓 세션 담아둘 맵
    private HashMap<String, WebSocketSession> sessionMap = new HashMap<>();
    // json 파싱 함수
    private static JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject object = null;

        try {
            object = (JSONObject) parser.parse(jsonStr);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지를 수신하면 실행 => 메세지 발송
        String msg = message.getPayload();
        JSONObject object = jsonToObjectParser(msg); // json 파싱 메소드에 들온 메세지 파싱

        for (String key : sessionMap.keySet()) {
            WebSocketSession webSocketSession = sessionMap.get(key);
            try {
                webSocketSession.sendMessage(new TextMessage(object.toJSONString()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 웹소켓 연결이 되면 동작 => 소켓 연결
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session); // 생성된 세션 저장

        // 웹 소캣이 처음 연결될 때 json값
        // js에서 엔터키를 누를 시 type이 message로 변경됨
        JSONObject object = new JSONObject();
        // 클라이언트단에서는 type값을 통해 메시지와 초기 설정값을 구분할 예정
        object.put("type", "getId");
        // 각 웹소켓 연결마다 고유하게 생성
        object.put("sessionId", session.getId());

        session.sendMessage(new TextMessage(object.toJSONString())); // 클라이언트단으로 발송
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 웹소켓이 종료되면 동작
        sessionMap.remove(session.getId()); // 세션 삭제
        super.afterConnectionClosed(session, status);
        
        
    }


}
