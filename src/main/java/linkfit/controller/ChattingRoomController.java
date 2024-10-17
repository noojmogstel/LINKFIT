package linkfit.controller;

import java.util.List;
import linkfit.annotation.Login;
import linkfit.dto.ChattingRoomRegisterRequest;
import linkfit.dto.Token;
import linkfit.service.ChattingRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        System.out.println(request.trainerId());
        chattingRoomService.addRoom(token.id(),token.role(),request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Long>> findJoinedRoom(@Login Token token) {
        List<Long> id = chattingRoomService.findJoinedRooms(token.id(),token.role());
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
