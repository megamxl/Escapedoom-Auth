package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.repositories.SessionManagementRepository;
import com.escapedoom.gamesession.services.PlayerStateManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/join")
public class TestController {

    private final PlayerStateManagementService playerStateManagementService;

    @GetMapping("/{escaperoom_id}")
    public String sessionId(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.mangeStateBySessionID(httpSession.getSession().getId(), escaperoom_id);
    }

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
