package com.escapedoom.auth.Service;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.OpenLobbys;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import com.escapedoom.auth.data.dataclasses.repositories.EscaperoomRepository;
import com.escapedoom.auth.data.dataclasses.repositories.LobbyRepository;
import com.escapedoom.auth.data.dtos.EscaperoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EscaperoomService {

    private final EscaperoomRepository escaperoomRepository;

    private final LobbyRepository lobbyRepository;

    private User getUser() {
         return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void createADummyRoom() {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Escaperoom dummy =
                Escaperoom.builder().user((User) user).build();
        escaperoomRepository.save(dummy);
    }

    public List<EscaperoomDTO> getAllRoomsByAnUser() {
        var rooms = escaperoomRepository.findEscaperoomByUser(getUser()).orElseThrow();
        List<EscaperoomDTO> ret = new ArrayList<>();
        rooms.stream().forEach(curr -> ret.add(new EscaperoomDTO(curr)));
        return ret;
    }

    public String startEscapeRoom(Long escapeRoomId) {
        var escaperoom = escaperoomRepository.getReferenceById(escapeRoomId);

        if (escaperoom != null && getUser() != null) {
            lobbyRepository.save(OpenLobbys.builder().escaperoom(escaperoom).user(getUser()).build());
            return "Done";
        } else {
            return null;
        }
    }

}
