package com.escapedoom.auth.Service;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.EscapeRoomState;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.OpenLobbys;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.*;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.ConsoleNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.DataNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.DetailsNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.ZoomNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import com.escapedoom.auth.data.dataclasses.repositories.EscaperoomRepository;
import com.escapedoom.auth.data.dataclasses.repositories.LobbyRepository;
import com.escapedoom.auth.data.dataclasses.repositories.TestRepo;
import com.escapedoom.auth.data.dtos.EscaperoomDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Configuration
public class EscaperoomService {

    private final EscaperoomRepository escaperoomRepository;

    private final LobbyRepository lobbyRepository;

    private final TestRepo repo;

    @Value("${gamesesion.url}")
    private String urlOfGameSession;

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public EscapeRoomDto createADummyRoom() {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Scenes> m = List.of(
                Scenes.builder()
                        .name("startScene")
                        .bgImg("https://www.noen.at/image/1920x1080-c-jpg/2282568/OPIC_007_%28C%29%20FH%20Campus%20Wien-David%20Bohmann%20%28Large%29.jpg")
                        .nodes(List.of(
                                Node.builder()
                                        .type(NodeType.Console)
                                        .pos(Position.builder()
                                                .x(250)
                                                .y(125)
                                                .build())
                                        .nodeInfos(ConsoleNodeInfo.builder()
                                                .outputID(12L)
                                                .codeSnipped("System.out.println(\"Hello World\")")
                                                .desc("I can only try one combination at a time. Find the correct one!")
                                                .returnType("4 digit integer")
                                                .exampleInput("1234")
                                                .png("png.url")
                                                .title("INPUT")
                                                .build())
                                        .build(),
                                Node.builder()
                                        .type(NodeType.Details)
                                        .pos(Position.builder()
                                                .x(250)
                                                .y(125)
                                                .build()
                                        )
                                        .nodeInfos(DetailsNodeInfo.builder()
                                                .desc("This item is really strange, I wonder if it still works...")
                                                .png("https://media.istockphoto.com/id/145132637/de/foto/alte-telefon.jpg?s=612x612&w=0&k=20&c=vKbE1neCPbp1AAdNZuW042vAxt7liMV52tEIAsHNjqs=")
                                                .title("An old Phone")
                                                .build())
                                        .build(),
                                Node.builder()
                                        .type(NodeType.Data)
                                        .pos(Position.builder().x(250).y(125).build()
                                        )
                                        .nodeInfos(DataNodeInfo.builder()
                                                .title("Object output")
                                                .desc("Some story like object description")
                                                .parameterType("A string containing the letters")
                                                .exampleOutput("ASDFGAIKVNAKSDNFJIVNHAEKW").build())
                                        .build(),
                                Node.builder()
                                        .type(NodeType.Zoom)
                                        .pos(Position.builder().x(250).y(125).build()
                                        )
                                        .nodeInfos(ZoomNodeInfo.builder().build())
                                        .build()
                                )
                        ).build()
        );
        Escaperoom dummy =
                Escaperoom.builder().user((User) user)
                        .name("Catch me")
                        .topic("Yee")
                        .time(90)
                        .build();

        var m2 = List.of(
                EscapeRoomStage.builder()
                        .stageId(1L)
                        .escaperoom(dummy)
                        .stage(m)
                        .build(),
                EscapeRoomStage.builder()
                        .stageId(2L)
                        .escaperoom(dummy)
                        .stage(m)
                        .build()
        );

        dummy.setEscapeRoomStages(m2);
        escaperoomRepository.save(dummy);
        return EscapeRoomDto.builder()
                .escaperoom_id(dummy.getEscaperoom_id())
                .name(dummy.getName())
                .topic(dummy.getTopic())
                .time(dummy.getTime())
                .escapeRoomStages(dummy.getEscapeRoomStages())
                .build();
    }


    @Transactional
    public EscapeRoomDto createADummyRoomForStart(User user) {
        List<Scenes> m = List.of(
                Scenes.builder()
                        .name("startScene")
                        .bgImg("https://www.noen.at/image/1920x1080-c-jpg/2282568/OPIC_007_%28C%29%20FH%20Campus%20Wien-David%20Bohmann%20%28Large%29.jpg")
                        .nodes(List.of(
                                        Node.builder()
                                                .type(NodeType.Console)
                                                .pos(Position.builder()
                                                        .x(250)
                                                        .y(125)
                                                        .build())
                                                .nodeInfos(ConsoleNodeInfo.builder()
                                                        .outputID(12L)
                                                        .codeSnipped("System.out.println(\"Hello World\")")
                                                        .desc("I can only try one combination at a time. Find the correct one!")
                                                        .returnType("4 digit integer")
                                                        .exampleInput("1234")
                                                        .png("png.url")
                                                        .title("INPUT")
                                                        .build())
                                                .build(),
                                        Node.builder()
                                                .type(NodeType.Details)
                                                .pos(Position.builder()
                                                        .x(250)
                                                        .y(125)
                                                        .build()
                                                )
                                                .nodeInfos(DetailsNodeInfo.builder()
                                                        .desc("This item is really strange, I wonder if it still works...")
                                                        .png("https://media.istockphoto.com/id/145132637/de/foto/alte-telefon.jpg?s=612x612&w=0&k=20&c=vKbE1neCPbp1AAdNZuW042vAxt7liMV52tEIAsHNjqs=")
                                                        .title("An old Phone")
                                                        .build())
                                                .build(),
                                        Node.builder()
                                                .type(NodeType.Data)
                                                .pos(Position.builder().x(250).y(125).build()
                                                )
                                                .nodeInfos(DataNodeInfo.builder()
                                                        .title("Object output")
                                                        .desc("Some story like object description")
                                                        .parameterType("A string containing the letters")
                                                        .exampleOutput("ASDFGAIKVNAKSDNFJIVNHAEKW").build())
                                                .build(),
                                        Node.builder()
                                                .type(NodeType.Zoom)
                                                .pos(Position.builder().x(250).y(125).build()
                                                )
                                                .nodeInfos(ZoomNodeInfo.builder().build())
                                                .build()
                                )
                        ).build()
        );
        Escaperoom dummy =
                Escaperoom.builder().user((User) user)
                        .name("Catch me")
                        .topic("Yee")
                        .time(90)
                        .build();

        var m2 = List.of(
                EscapeRoomStage.builder()
                        .stageId(1L)
                        .escaperoom(dummy)
                        .stage(m)
                        .build(),
                EscapeRoomStage.builder()
                        .stageId(2L)
                        .escaperoom(dummy)
                        .stage(m)
                        .build()
        );

        dummy.setEscapeRoomStages(m2);
        escaperoomRepository.save(dummy);
        return EscapeRoomDto.builder()
                .escaperoom_id(dummy.getEscaperoom_id())
                .name(dummy.getName())
                .topic(dummy.getTopic())
                .time(dummy.getTime())
                .escapeRoomStages(dummy.getEscapeRoomStages())
                .build();
    }

    public List<EscaperoomDTO> getAllRoomsByAnUser() {
        var rooms = escaperoomRepository.findEscaperoomByUser(getUser()).orElseThrow();
        List<EscaperoomDTO> ret = new ArrayList<>();
        for (Escaperoom escaperoom : rooms) {
            var m = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscaperoom_id(), getUser());
            EscapeRoomState escapeRoomState = EscapeRoomState.STOPPED;
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
            var curr = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscaperoom_id(), getUser());
            if (curr.isPresent()) {
                if (curr.get().getState() != EscapeRoomState.STOPPED) {
                    return curr.get().getLobby_Id().toString();
                }
            }
            var newRoom = lobbyRepository.save(OpenLobbys.builder().escaperoom(escaperoom).user(getUser()).state(EscapeRoomState.JOINABLE).build());
            return newRoom.getLobby_Id().toString();
        } else {
            return null;
        }
    }

    public String changeEscapeRoomState(Long escapeRoomId, EscapeRoomState escapeRoomState) {
        var escaperoom = escaperoomRepository.getReferenceById(escapeRoomId);

        if (escaperoom != null && getUser() != null) {
            OpenLobbys openLobbys = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscaperoom_id(), getUser()).get();
            openLobbys.setState(escapeRoomState);
            lobbyRepository.save(openLobbys);
            lobbyRepository.flush();
            if (escapeRoomState == EscapeRoomState.PLAYING) {
                informSession(openLobbys.getLobby_Id());
            }
            return "Stopped EscapeRoom with ID";
        } else {
            return null;
        }
    }

    private void informSession(Long id) {

        //TODO Change url to configuration

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlOfGameSession+"/info/started/" + id)
                .build(); // defaults to GET
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static SessionFactory getCurrentSessionFromJPA() {
        // JPA and Hibernate SessionFactory example
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("entityManager");
        EntityManager entityManager = emf.createEntityManager();
        // Get the Hibernate Session from the EntityManager in JPA
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        SessionFactory factory = session.getSessionFactory();
        return factory;
    }

}
