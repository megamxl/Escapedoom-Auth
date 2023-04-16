package com.escapedoom.auth.demoCont;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/escape-room")
@RequiredArgsConstructor
public class DemoCont {

    @GetMapping("/getAll")
    ResponseEntity<String> getAllEscapeRoom() {
        return ResponseEntity.ok("here would be the escape rooms");
    }

}
