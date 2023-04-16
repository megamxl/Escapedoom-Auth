package com.escapedoom.auth.controller;

import com.escapedoom.auth.Service.AuthenticationService;
import com.escapedoom.auth.dataclasses.Requests.AuthenticationRequest;
import com.escapedoom.auth.dataclasses.Requests.RegisterRequest;
import com.escapedoom.auth.dataclasses.Respones.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginAndRegisterController {

    private final AuthenticationService authenticationService;

    @RestController
    @RequestMapping("/api/v1/auth")
    @RequiredArgsConstructor
    public class AuthenticationController {

        private final AuthenticationService service;

        @PostMapping("/register")
        public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
            return ResponseEntity.ok(service.register(request));
        }

        @PostMapping("/authenticate")
        public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
            return ResponseEntity.ok(service.authenticate(request));
        }

        @GetMapping("/hello")
        public ResponseEntity<String> sayHello() {

            return ResponseEntity.ok("hello");
        }

    }
}
