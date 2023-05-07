package com.escapedoom.gamesession.services;

import com.escapedoom.gamesession.SseEmitterExtended;
import com.escapedoom.gamesession.data.EscapeRoomState;
import com.escapedoom.gamesession.data.OpenLobbys;
import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.repositories.OpenLobbyRepository;
import com.escapedoom.gamesession.repositories.SessionManagementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerStateManagementService {

    private final SessionManagementRepository sessionManagementRepository;

    private final OpenLobbyRepository openLobbyRepository;

    public List<SseEmitterExtended> sseEmitters = new CopyOnWriteArrayList<>();


    private ArrayList<String> firstNames = new ArrayList<>() {{
        add("Shadow");
        add("Dark");
        add("Crimson");
        add("Blaze");
        add("Frost");
        add("Mystic");
        add("Iron");
        add("Silver");
        add("Void");
        add("Sky");
        add("Moonlight");
        add("Blood");
        add("Night");
        add("Ice");
        add("Phoenix");
        add("Ghost");
        add("Dragon");
    }};

    private ArrayList<String> secondNames = new ArrayList<>() {{
        add("Blade");
        add("Night");
        add("Viper");
        add("Fire");
        add("Fang");
        add("Gaze");
        add("Fist");
        add("Storm");
        add("Walker");
        add("Watcher");
        add("Assassin");
        add("Moon");
        add("Wing");
        add("Slayer");
        add("Queen");
        add("Rider");
        add("Bringer");
        add("Reaper");
        add("Slayer");
    }};

    private Random random =new Random();

    public SseEmitterExtended mangeStateBySessionID(String httpSessionID, Long escaperoomSession)  {

        Optional<OpenLobbys> lobbyOpt = openLobbyRepository.findByLobbyId(escaperoomSession);
        OpenLobbys lobby = null;
        if (lobbyOpt.isEmpty()) {
            return null;
        }else {
            lobby = lobbyOpt.get();
        }

        Player player = sessionManagementRepository.findPlayerByHttpSessionID(httpSessionID);
        if (player != null) {
            switch (lobby.getState()) {
                case JOINABLE -> {
                    //TODO FIND EMITER WITH ID DELETE AND REPLACE
                }
                case PLAYING -> {
                    //RETURN THE CURRENT STATE
                }
                case STOPPED -> {
                    return null;
                }
            }
        } else {
            player = Player.builder()
                    .name(getRandomName())
                    .httpSessionID(httpSessionID)
                    .escaperoomSession(escaperoomSession)
                    .build();
            sessionManagementRepository.save(player);
            //TODO return the last saved state
        }

        SseEmitterExtended sseEmitter = new SseEmitterExtended();
        sseEmitter.setHttpID(httpSessionID);
        sseEmitter.setLobby_id(escaperoomSession);
        sseEmitter.setName(player.getName());
        try {
            sseEmitter.send(SseEmitter.event().name("yourName").data(sseEmitter.getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sseEmitters.add(sseEmitter);

        var players = sessionManagementRepository.findAllByEscaperoomSession(escaperoomSession);

        for (SseEmitterExtended sseEmitterExtended : sseEmitters) {
            if (players.isPresent()) {
                players.get().stream().filter(player1 -> Objects.equals(player1.getEscaperoomSession(), escaperoomSession));
                if (sseEmitterExtended.getLobby_id().equals(escaperoomSession)) {
                    try {
                        sseEmitter.send(SseEmitter.event().name("allNames").data(players.get().stream().map(Player::getName).collect(Collectors.toList())));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return sseEmitter;
    }
    @Transactional
    public String  deleteAllPlayersByEscaperoomID(Long id) {
        sessionManagementRepository.deleteAllByEscaperoomSession(id);
        return "done";
    }

    public List<Player> getAllPlayersByEscapeRoomID(Long id) {
        return sessionManagementRepository.findAllByEscaperoomSession(id).orElseThrow();
    }

    private String getRandomName() {
        return firstNames.get((random.nextInt(0, firstNames.size()))) + secondNames.get(random.nextInt(0, secondNames.size()));
    }

    public void informAboutStart(Long id) {
        if (openLobbyRepository.findByLobbyId(id).get().getState() ==  EscapeRoomState.PLAYING) {

            //TODO inform all clients that game has started
            System.out.println("informing clients");

        }

    }


}
