package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.SseEmitterExtended;
import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.services.PlayerStateManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/join")
public class TestController {

    private final PlayerStateManagementService playerStateManagementService;


    @CrossOrigin
    // method for joining / subscribing
    @GetMapping(value = "/{escaperoom_id}")
    public SseEmitter sessionId(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
       var  sse=  playerStateManagementService.mangeStateBySessionID(httpSession.getSession().getId(), escaperoom_id);
        if (sse != null) {
            return sse;
        } else {
            return null;
        }
        //TODO if (false /*chack if  player  is already in sse ppol or*/)

    }
    // method to dispatch data to the rigth lobbys

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping("delete/{escaperoom_id}")
    public String  deletAll(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.deleteAllPlayersByEscaperoomID(escaperoom_id);
    }

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping("getAll/{escaperoom_id}")
    public List<Player> getAll(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.getAllPlayersByEscapeRoomID(escaperoom_id);
    }

}
