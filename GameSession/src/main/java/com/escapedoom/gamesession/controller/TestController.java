package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.repositories.SessionManagementRepository;
import com.escapedoom.gamesession.services.PlayerStateManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TestController {

    private final PlayerStateManagementService playerStateManagementService;

    @GetMapping("/t")
    public String sessionId(HttpServletRequest httpSession){
        return playerStateManagementService.mangeStateBySessionID(httpSession.getSession().getId());
    }

}
