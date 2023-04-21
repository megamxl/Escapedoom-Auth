package com.escapedoom.gamesession.services;

import com.escapedoom.gamesession.data.EscapeRoomState;
import com.escapedoom.gamesession.data.OpenLobbys;
import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.repositories.OpenLobbyRepository;
import com.escapedoom.gamesession.repositories.SessionManagementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PlayerStateManagementService {

    private final SessionManagementRepository sessionManagementRepository;

    private final OpenLobbyRepository openLobbyRepository;

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

    public String mangeStateBySessionID(String httpSessionID, Long escaperoomSession)  {
        //TODO check if the escaperommSessioni joinable
        OpenLobbys lobby = openLobbyRepository.findByLobbyId(escaperoomSession).get();
        if (lobby.getState() == EscapeRoomState.STOPED ) {
            return "This room isn't open";
        }

        Player player = sessionManagementRepository.findPlayerByHttpSessionID(httpSessionID);
        if (player != null) {
            if (lobby.getState() == EscapeRoomState.PLAYING) {
                //TODO return the last saved state
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
        return player.getName();
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


}
