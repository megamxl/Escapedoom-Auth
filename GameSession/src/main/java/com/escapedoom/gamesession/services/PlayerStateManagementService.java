package com.escapedoom.gamesession.services;

import com.escapedoom.gamesession.data.Player;
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
        Player player = sessionManagementRepository.findPlayerByHttpSessionID(httpSessionID);
        if (player != null) {
            return player.getName();
        } else {
            player = Player.builder()
                    .name(getRandomName())
                    .httpSessionID(httpSessionID)
                    .escaperoomSession(escaperoomSession)
                    .build();
            sessionManagementRepository.save(player);
            return player.getName();
        }
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
