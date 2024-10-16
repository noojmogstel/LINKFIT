package linkfit.controller;

import linkfit.annotation.Login;
import linkfit.dto.ChattingRoomRegisterRequest;
import linkfit.dto.Token;
import linkfit.service.ChattingRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatting")
public class ChattingRoomController {

    ChattingRoomService chattingRoomService;

    public ChattingRoomController(ChattingRoomService chattingRoomService) {
        this.chattingRoomService = chattingRoomService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addChattingRoom(@Login Token token, @RequestBody
        ChattingRoomRegisterRequest request) {
        chattingRoomService.addRoom(token.id(),token.role(),request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
