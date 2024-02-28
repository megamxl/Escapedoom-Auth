package com.escapedoom.auth;

import com.escapedoom.auth.Service.AuthenticationService;
import com.escapedoom.auth.Service.EscaperoomService;
import com.escapedoom.auth.data.dataclasses.Requests.RegisterRequest;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import com.escapedoom.auth.data.dataclasses.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuthApplication implements CommandLineRunner {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EscaperoomService escaperoomService;

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate6Module());
        return objectMapper;
    }


    @Override
    public void run(String... args) throws Exception {

        authenticationService.register(
                RegisterRequest.builder()
                .firstname("Leon")
                .lastname("FreudenThaler")
                .email("leon@escapeddoom.com")
                .password("escapeDoom")
                .build());
        authenticationService.register(
                RegisterRequest.builder()
                .firstname("Bernhard")
                .lastname("Taufner")
                .email("bernhard@escapeddoom.com")
                .password("escapeDoom")
                .build());

//        escaperoomService.createADummyRoomForStart(userRepository.findByEmail("bernhard@escapeddoom.com").get());
//        escaperoomService.createADummyRoomForStart(userRepository.findByEmail("bernhard@escapeddoom.com").get());
         escaperoomService.createADummyRoomForStart(userRepository.findByEmail("bernhard@escapeddoom.com").get());
//
//        escaperoomService.createADummyRoomForStart(userRepository.findByEmail("leon@escapeddoom.com").get());
//        escaperoomService.createADummyRoomForStart(userRepository.findByEmail("leon@escapeddoom.com").get());
        escaperoomService.createADummyRoomForStart(userRepository.findByEmail("leon@escapeddoom.com").get());


    }
}
