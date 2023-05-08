package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.SseEmitterExtended;
import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.data.response.JoinResponse;
import com.escapedoom.gamesession.services.PlayerStateManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/join")
public class JoinController {

    private final PlayerStateManagementService playerStateManagementService;


    @CrossOrigin
    // method for joining / subscribing
    @GetMapping(value = "/{escaperoom_id}", consumes = MediaType.ALL_VALUE)
    public JoinResponse sessionId(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        var  name=  playerStateManagementService.mangeStateBySessionID(httpSession.getSession().getId(), escaperoom_id);
        if (name != null) {
            return JoinResponse.builder().name(name).sessionId(httpSession.getSession().getId()).build();
        } else {
            return null;
        }
        //TODO if (false /*chack if  player  is already in sse ppol or*/)
    }

    @GetMapping(value = "lobby/{id}")
    public SseEmitterExtended lobby(@PathVariable("id")String session) {
        return playerStateManagementService.lobbyConnection(session);
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
