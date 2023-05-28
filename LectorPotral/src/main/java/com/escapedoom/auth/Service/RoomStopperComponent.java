package com.escapedoom.auth.Service;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.EscapeRoomState;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.OpenLobbys;
import com.escapedoom.auth.data.dataclasses.repositories.LobbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomStopperComponent {

    private final LobbyRepository lobbyRepository;

    //@Scheduled(fixedRate = 60000)
    @Scheduled(fixedRate = 6000)
    public void HouseKeeper() {
        System.out.println("in check");
        try {
            Optional<List<OpenLobbys>> allByStatePlaying = lobbyRepository.findAllByStatePlaying();
            if (allByStatePlaying.isPresent()) {
                for (OpenLobbys openLobbys : allByStatePlaying.get()) {
                    if (LocalDateTime.now().isAfter(openLobbys.getEndTime())) {
                        System.out.println("checked gets changed");
                        openLobbys.setState(EscapeRoomState.STOPPED);
                        lobbyRepository.save(openLobbys);
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}
