package com.escapedoom.auth.controller;

import com.escapedoom.auth.Service.EscaperoomService;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.escapedoom.auth.data.dtos.EscaperoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        var  m = SecurityContextHolder.getContext();
        escaperoomService.createADummyRoom();
        return ResponseEntity.ok("here would be the escape rooms");
    }

}
