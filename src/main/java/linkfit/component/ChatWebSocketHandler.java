package linkfit.component;

import java.io.IOException;
import java.util.Collection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();  // ObjectMapper 인스턴스

    public ChatWebSocketHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = session.getUri().getPath();
        Long roomId = extractRoomIdFromPath(path);
        Long userId = extractUserIdFromPath(path);
        String role = extractRoleFromPath(path); // role을 경로에서 추출

        // 세션을 userId와 role로 구분하여 추가
        sessionManager.addSession(roomId, userId, role, session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String path = session.getUri().getPath();
        Long roomId = extractRoomIdFromPath(path);
        Long userId = extractUserIdFromPath(path);
        String role = extractRoleFromPath(path); // role 추출

        // 메시지 처리
        JsonNode jsonNode = objectMapper.readTree(message.getPayload());  // JSON 파싱
        String content = jsonNode.get("content").asText();  // content 필드 추출

        // 같은 방에 있는 모든 사용자에게 메시지 브로드캐스트
        broadcastMessageToRoom(roomId, new TextMessage(message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
        throws Exception {
        String path = session.getUri().getPath();
        Long roomId = extractRoomIdFromPath(path);
        Long userId = extractUserIdFromPath(path);
        String role = extractRoleFromPath(path); // role 추출

        // 세션에서 userId와 role로 세션 제거
        sessionManager.removeSession(roomId, userId, role);
    }

    // 경로에서 roomId, userId와 role을 추출하는 유틸리티 메서드
    private Long extractRoomIdFromPath(String path) {
        return Long.parseLong(path.split("/")[2]);
    }

    private Long extractUserIdFromPath(String path) {
        return Long.parseLong(path.split("/")[3]);
    }

    private String extractRoleFromPath(String path) {
        return path.split("/")[4]; // role을 URL 경로에서 추출
    }

    // 방에 있는 사용자들에게 메시지를 브로드캐스트하는 메서드
    private void broadcastMessageToRoom(Long roomId, TextMessage message) throws IOException {
        Collection<WebSocketSession> sessions = sessionManager.getSessions(roomId);
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(message);
            }
        }
    }
}
