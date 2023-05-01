package com.escapedoom.auth.Service;

import com.escapedoom.auth.data.dataclasses.Respones.AuthenticationResponse;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.EscapeRoomState;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.OpenLobbys;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import com.escapedoom.auth.data.dataclasses.repositories.EscaperoomRepository;
import com.escapedoom.auth.data.dataclasses.repositories.LobbyRepository;
import com.escapedoom.auth.data.dtos.EscaperoomDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
                Escaperoom.builder().user((User) user).
                        name("Catch me")
                        .topic("Yee")
                        .time(90)
                        .build();
        escaperoomRepository.save(dummy);
    }

    public List<EscaperoomDTO> getAllRoomsByAnUser() {
        var rooms = escaperoomRepository.findEscaperoomByUser(getUser()).orElseThrow();
        List<EscaperoomDTO> ret = new ArrayList<>();
        for (Escaperoom escaperoom : rooms) {
            var m = lobbyRepository.findByEscaperoomAndUserAndStateStopedNot(escaperoom.getEscaperoom_id(), getUser());
            EscapeRoomState escapeRoomState = EscapeRoomState.STOPED;
            if (m.isPresent()) {
                escapeRoomState = m.get().getState();
            }
            ret.add(new EscaperoomDTO(escaperoom, escapeRoomState));
        }

        return ret;
    }

    public String openEscapeRoom(Long escapeRoomId) {
        var escaperoom = escaperoomRepository.getReferenceById(escapeRoomId);

        if (escaperoom != null && getUser() != null) {
            var curr = lobbyRepository.findByEscaperoomAndUserAndStateStopedNot(escaperoom.getEscaperoom_id(), getUser());
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
            OpenLobbys openLobbys = lobbyRepository.findByEscaperoomAndUserAndStateStopedNot(escaperoom.getEscaperoom_id(), getUser()).get();
            openLobbys.setState(escapeRoomState);
            lobbyRepository.save(openLobbys);
            if (escapeRoomState == EscapeRoomState.PLAYING) {
                informSession(openLobbys.getLobby_Id());
            }
            return "Stoped EscapeRoom with ID";
        } else {
            return null;
        }
    }

    private void informSession(Long id) {

        //TODO Change url to configuration

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8090/info/started/"+id)
                .build(); // defaults to GET
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
