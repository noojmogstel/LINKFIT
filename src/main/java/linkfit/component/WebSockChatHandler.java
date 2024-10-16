package linkfit.component;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSockChatHandler extends TextWebSocketHandler {

    @Override // 웹 소켓 연결시
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }


    @Override // 웹소켓 통신 에러시
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception {
        String payload = message.getPayload();
        System.out.println("Received: " + payload);

        TextMessage textMessage = new TextMessage("Hello from server!");
        session.sendMessage(textMessage);
    }
}
