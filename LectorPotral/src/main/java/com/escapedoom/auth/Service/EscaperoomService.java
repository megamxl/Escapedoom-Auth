package com.escapedoom.auth.Service;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.EscapeRoomState;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.OpenLobbys;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import com.escapedoom.auth.data.dataclasses.repositories.EscaperoomRepository;
import com.escapedoom.auth.data.dataclasses.repositories.LobbyRepository;
import com.escapedoom.auth.data.dtos.EscaperoomDTO;
import jakarta.transaction.Transactional;
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

    public String openEscapeRoom(Long escapeRoomId) {
        var escaperoom = escaperoomRepository.getReferenceById(escapeRoomId);

        if (escaperoom != null && getUser() != null) {
            var curr = lobbyRepository.findByEscaperoomAndUserAndStateStopedNot(escaperoom, getUser());
            if (curr.isPresent()) {
                if (curr.get().getState() != EscapeRoomState.STOPED) {
                    return curr.get().getLobby_Id().toString();
                }
            }
            var newRoom = lobbyRepository.save(OpenLobbys.builder().escaperoom(escaperoom).user(getUser()).state(EscapeRoomState.JOINABLE).build());
            return newRoom.getLobby_Id().toString();
        } else {
            return null;
        }
    }


    @Transactional
    public String changeEscapeRoomState(Long escapeRoomId, EscapeRoomState escapeRoomState) {
        var escaperoom = escaperoomRepository.getReferenceById(escapeRoomId);

        if (escaperoom != null && getUser() != null) {
            OpenLobbys openLobbys = lobbyRepository.findByEscaperoomAndUserAndStateStopedNot(escaperoom, getUser()).get();
            openLobbys.setState(escapeRoomState);
            lobbyRepository.save(openLobbys);
            return "Stoped EscapeRoom with ID";
        } else {
            return null;
        }
    }

}
