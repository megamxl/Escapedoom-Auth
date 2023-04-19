package com.escapedoom.gamesession.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/t")
    public String sessionId(HttpServletRequest httpSession){

        HttpSession session =  httpSession.getSession();
        return session.getId();
    }

}
