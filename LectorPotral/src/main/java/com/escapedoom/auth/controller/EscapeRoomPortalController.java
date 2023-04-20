package com.escapedoom.auth.controller;

import com.escapedoom.auth.Service.EscaperoomService;
import com.escapedoom.auth.data.dtos.EscaperoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portal-escape-room")
@RequiredArgsConstructor
public class EscapeRoomPortalController {

    private final EscaperoomService escaperoomService;

    @GetMapping("/getAll")
    ResponseEntity<List<EscaperoomDTO>> getAllEscapeRoom() {
        return ResponseEntity.ok(escaperoomService.getAllRoomsByAnUser());
    }

    @PostMapping("/saveEscaperoom")
    ResponseEntity<String> saveEscapeRoom() {
        escaperoomService.createADummyRoom();
        return ResponseEntity.ok("here would be the escape rooms");
    }

    @PostMapping(value = "startEscapeRoom/{escapeRoomId}")
    public ResponseEntity<String> startEscapeRoom(@PathVariable("escapeRoomId") Long lobbyId) {
        return ResponseEntity.ok(escaperoomService.startEscapeRoom(lobbyId));
    }

    @PostMapping(value = "stopEscapeRoom/{escapeRoomId}")
    public ResponseEntity<String> stopEscapeRoom(@PathVariable("escapeRoomId") Long lobbyId) {
        return ResponseEntity.ok(escaperoomService.stopEscapeRoom(lobbyId));
    }

}
