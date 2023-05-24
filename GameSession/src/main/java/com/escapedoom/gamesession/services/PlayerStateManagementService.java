package com.escapedoom.gamesession.services;

import com.escapedoom.gamesession.CodeSniptes;
import com.escapedoom.gamesession.SseEmitterExtended;
import com.escapedoom.gamesession.configuration.redis.KafkaConfigProperties;
import com.escapedoom.gamesession.data.EscapeRoomState;
import com.escapedoom.gamesession.data.OpenLobbys;
import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.data.codeCompiling.CodeCompilingRequestEvent;
import com.escapedoom.gamesession.data.codeCompiling.CodingLanguage;
import com.escapedoom.gamesession.repositories.OpenLobbyRepository;
import com.escapedoom.gamesession.repositories.SessionManagementRepository;
import com.escapedoom.gamesession.repositories.TestRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerStateManagementService {

    private final SessionManagementRepository sessionManagementRepository;

    private final OpenLobbyRepository openLobbyRepository;

    private final TestRepo repo;

    private final KafkaTemplate<String,String> kafkaTemplate;

    private final KafkaConfigProperties kafkaConfigProperties;

    private final ObjectMapper myJsonSlave;


    private final String ALL_NAME_EVENT = "allNames";
    private final String YOUR_NAME_EVENT = "yourName";
    private final String START_PLAYING_EVENT = "started";
    public final List<SseEmitterExtended> sseEmitters = new CopyOnWriteArrayList<>();

    private boolean update = false;


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

    private final Random random = new Random();

    public String mangeStateBySessionID(String httpSessionID, Long escaperoomSession) {

        Optional<OpenLobbys> lobbyOpt = openLobbyRepository.findByLobbyId(escaperoomSession);
        OpenLobbys lobby = null;
        if (lobbyOpt.isEmpty()) {
            return null;
        } else {
            lobby = lobbyOpt.get();
        }

        var optplayer = sessionManagementRepository.findPlayerByHttpSessionID(httpSessionID);
        Player player;
        if (optplayer.isPresent()) {
            player = optplayer.get();
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
                    .escampeRoom_room_id(lobby.getEscaperoom_escaperoom_id())
                    .httpSessionID(httpSessionID)
                    .escaperoomSession(escaperoomSession)
                    .escaperoomStageId(1L)
                    .build();
            sessionManagementRepository.save(player);
            //TODO return the last saved state
        }

        update = true;
        return player.getName();
    }

    private void inforAllPlayersAboutPlayers(Player player, Boolean playing) {
        var jsonPlayers = new JSONObject();
        if (playing) {
            informClients(player.getEscaperoomSession(), START_PLAYING_EVENT, new JSONObject());
        } else {
            var players = sessionManagementRepository.findAllByEscaperoomSession(player.getEscaperoomSession());
            if (players.isPresent()) {
                players.get().stream().filter(player1 -> Objects.equals(player1.getEscaperoomSession(), player.getEscaperoomSession()));
                jsonPlayers.put("players", players.get().stream().map(Player::getName).collect(Collectors.toList()));
            }
            informClients(player.getEscaperoomSession(), ALL_NAME_EVENT, jsonPlayers.toString());
        }
    }

    public SseEmitterExtended lobbyConnection(String httpId) {

        SseEmitterExtended sseEmitter = new SseEmitterExtended();
        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
            sseEmitters.remove(sseEmitter);
        });
        Player player ;
        var optplayer = sessionManagementRepository.findPlayerByHttpSessionID(httpId);
        if (optplayer.isPresent()) {
            player = optplayer.get();
        } else {
            return null;
        }

        sseEmitter.setHttpID(httpId);
        sseEmitter.setLobby_id(player.getEscaperoomSession());
        sseEmitter.setName(player.getName());
        sseEmitters.add(sseEmitter);


        try {
            sseEmitter.send(SseEmitter.event().name(YOUR_NAME_EVENT).data(sseEmitter.getName()));
            var players = sessionManagementRepository.findAllByEscaperoomSession(player.getEscaperoomSession());
            var jsonPlayers = new JSONObject();
            if (players.isPresent()) {
                players.get().stream().filter(player1 -> Objects.equals(player1.getEscaperoomSession(), player.getEscaperoomSession()));
                jsonPlayers.put("players", players.get().stream().map(Player::getName).collect(Collectors.toList()));
            }
            sseEmitter.send(SseEmitter.event().name(ALL_NAME_EVENT).data(jsonPlayers.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        sseEmitter.onCompletion(() -> {
            synchronized (this.sseEmitters) {
                this.sseEmitters.remove(sseEmitter);
            }
        });
        if (update) {
            inforAllPlayersAboutPlayers(player, false);
            update = false;
        }
        return sseEmitter;
    }

    public void informClients(Long esceproomId, String eventName, Object toSend) {
        if (sseEmitters.isEmpty()) {
            log.atInfo().log("No active Emitters ");
            return;
        }

        ArrayList<SseEmitterExtended> failure = new ArrayList<>();
        for (SseEmitterExtended sseEmitterExtended : sseEmitters) {
            if (sseEmitterExtended != null) {
                try {
                    if (sseEmitterExtended.getLobby_id() == esceproomId) {
                        sseEmitterExtended.send(SseEmitter.event().name(eventName).data(toSend));
                        sseEmitterExtended.complete();
                    }
                } catch (Exception ignored) {
                    failure.add(sseEmitterExtended);
                }
                if (!failure.isEmpty()) {
                    sseEmitters.removeAll(failure);
                }
            }
        }
    }


    @Transactional
    public String deleteAllPlayersByEscaperoomID(Long id) {
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
        if (openLobbyRepository.findByLobbyId(id).get().getState() == EscapeRoomState.PLAYING) {

            Optional<List<Player>> allByEscaperoomSession = sessionManagementRepository.findAllByEscaperoomSession(id);
            if (allByEscaperoomSession.isPresent()) {

                // Added this since a partial restart of the system caused no new players to join :(
                if (allByEscaperoomSession.get().size() != 0) {
                    inforAllPlayersAboutPlayers(allByEscaperoomSession.get().get(0),true);
                    System.out.println("informing clients");
                } else {
                    System.out.println("No player found!");
                }
            }

        }

    }
    public ArrayList<Object> returnStageToPlayer(String httpSession) {

        var curr = sessionManagementRepository.findPlayerByHttpSessionID(httpSession);
        if (curr.isPresent()) {
            return repo.getEscapeRoomStageByEscaperoomIDAndStageNumber(curr.get().getEscampeRoom_room_id(), curr.get().getEscaperoomStageId());
        } else {
            return null;
        }
    }

    public void startCompiling(CodeCompilingRequestEvent codeCompilingRequestEvent) {
       /*final CodeCompilingRequestEvent codeCompilingRequestEvent=  CodeCompilingRequestEvent.builder()
                .playerSessionId(sessionId.toLowerCase())
                .dateTime(LocalDateTime.now())
                .language(CodingLanguage.Java)
                .code(
                        "import java.util.ArrayList;\n" +
                                "import java.util.List;\n" +
                                "import java.util.Random;\n" +
                                "import java.util.concurrent.atomic.LongAdder;\n" +
                                "\n" +
                                "public class app {\n" +
                                "    public static void main(String[] args) {\n" +
                                "        System.out.println(\"Hello Thommy\");\n" +
                                "        System.out.println(35+25);\n" +
                                "    }\n" +
                                "}"
                )
                .build();
                */
        String requestAsJsoString = null;

        codeCompilingRequestEvent.setDateTime(LocalDateTime.now());
        codeCompilingRequestEvent.setCode(CodeSniptes.javaClassGenerator(codeCompilingRequestEvent.getCode()));

        try {
            requestAsJsoString = myJsonSlave.writeValueAsString(codeCompilingRequestEvent);
        } catch (JsonProcessingException e) {
            return;
        }
        kafkaTemplate.send(kafkaConfigProperties.getCodeCompilerTopic(), requestAsJsoString);

    }


}
