package com.escapedoom.auth.Service;

import com.escapedoom.auth.data.dataclasses.Requests.RegisterRequest;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.*;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.*;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.ConsoleNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.DataNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.DetailsNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.ZoomNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import com.escapedoom.auth.data.dataclasses.repositories.*;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Configuration
public class EscaperoomService {

    private final EscaperoomRepository escaperoomRepository;

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;

    private final LobbyRepository lobbyRepository;

    private final CodeRiddleRepository codeRiddleRepository;

    @Value("${gamesesion.url}")
    private String urlOfGameSession;

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public EscapeRoomDto createADummyRoom() {
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

        createADummyRoomForStart(userRepository.findByEmail("bernhard@escapeddoom.com").get());
        createADummyRoomForStart(userRepository.findByEmail("bernhard@escapeddoom.com").get());
        createADummyRoomForStart(userRepository.findByEmail("bernhard@escapeddoom.com").get());

        createADummyRoomForStart(userRepository.findByEmail("leon@escapeddoom.com").get());
        createADummyRoomForStart(userRepository.findByEmail("leon@escapeddoom.com").get());
        createADummyRoomForStart(userRepository.findByEmail("leon@escapeddoom.com").get());

        return null;
    }


    @Transactional
    public EscapeRoomDto createADummyRoomForStart(User user) {

        ConsoleNodeCode save = codeRiddleRepository.save(ConsoleNodeCode.builder()
                .language(CodingLanguage.Java)
                .functionSignature("/**\n" +
                        "* @param boardInput the input string\n" +
                        "* @return the message you need\n" +
                        "*/\n" +
                        "public static String solve(String boardInput) {\n\n}")
                .input("public static String boardInput = \"lipps$M$Eq$mrxiviwxih$mr$Wlmjxmrk\"; \n\n")
                .expectedOutput("hello I Am interested in Shifting")
                .variableName("boardInput")
                .build());

        ConsoleNodeCode save2 = codeRiddleRepository.save(ConsoleNodeCode.builder()
                .language(CodingLanguage.Java)
                .functionSignature("/**\n" +
                        "* @param input is a List of Lists of Booleans \n" +
                        "*              Example \n" +
                        "*              [\n" +
                        "*                  [true,true,false,true],\n" +
                        "*                  [false,true,false,true,true,true],\n" +
                        "*                  [true,true],\n" +
                        "*              ]\n" +
                        "* @return the current Floor\n" +
                        "*/\n" +
                        "public static int solve(List<List<Boolean>> input) {\n\n}")
                .input("     public static List<List<Boolean>> listOfBinary = List.of(\n" +
                        "                List.of(true, false, false, false, true, true),\n" +
                        "                List.of(true, true, false, false),\n" +
                        "                List.of(true,true,false,true,false,true,true,true,false),\n" +
                        "                List.of(true,true,false,true),\n" +
                        "                List.of(true,true,false,true,true),\n" +
                        "                List.of(true,false,false,true,false,true,false,true,false,false),\n" +
                        "                List.of(true,false,false,false,false,false,false,false),\n" +
                        "                List.of(true,false,false,true,false,false,false,false,true),\n" +
                        "                List.of(true,false,false,false,true,true,true,true,false)\n" +
                        "        ); \n\n")
                .expectedOutput("4")
                .variableName("listOfBinary")
                .build());



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
                                                        .outputID(save.getId())
                                                        .codeSnipped(save.getFunctionSignature())
                                                        .desc("I can only try one combination at a time. Find the correct one!")
                                                        .returnType("4 digit integer")
                                                        .exampleInput(save.getInput())
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


        List<Scenes> m3 = List.of(
                Scenes.builder()
                        .name("secondScene")
                        .bgImg("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsZSaFZ-nKGSkIEane9_ucnaElVrGkxLlOyQ&usqp=CAU")
                        .nodes(List.of(
                                        Node.builder()
                                                .type(NodeType.Console)
                                                .pos(Position.builder()
                                                        .x(250)
                                                        .y(125)
                                                        .build())
                                                .nodeInfos(ConsoleNodeInfo.builder()
                                                        .outputID(save2.getId())
                                                        .codeSnipped(save2.getFunctionSignature())
                                                        .desc("I can only see true and false I have seen this format before ")
                                                        .returnType("The current floor as integer")
                                                        .exampleInput("[ [true,false,true], [true,false,false,false] ]")
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
                                                        .desc("I can see that the even sum needs to be subtracted form the odd sum")
                                                        .png("https://de.wikipedia.org/wiki/Matt_Riddle#/media/Datei:Matt_Riddle_August_2017.jpg")
                                                        .title("An Old Chad")
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
                        .escapeRoomStages(Collections.emptyList())
                        .time(90)
                        .build();


        var m2 = List.of(
                EscapeRoomStage.builder()
                        .stageId(1L)
                        .outputID(save.getId())
                        .escaperoom(dummy)
                        .stage(m)
                        .build(),
                EscapeRoomStage.builder()
                        .stageId(2L)
                        .outputID(save2.getId())
                        .escaperoom(dummy)
                        .stage(m3)
                        .build()
        );

        escaperoomRepository.save(dummy);
        dummy.setEscapeRoomStages(m2);
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

    public String changeEscapeRoomState(Long escapeRoomId, EscapeRoomState escapeRoomState, Long time) {
        var escaperoom = escaperoomRepository.getReferenceById(escapeRoomId);

        if (escaperoom != null && getUser() != null) {
            OpenLobbys openLobbys = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscaperoom_id(), getUser()).get();
            openLobbys.setState(escapeRoomState);
            lobbyRepository.flush();
            lobbyRepository.save(openLobbys);
            if (escapeRoomState == EscapeRoomState.PLAYING) {
                openLobbys.setEndTime(LocalDateTime.now().plusMinutes(time));
                openLobbys.setStartTime(LocalDateTime.now());
                lobbyRepository.flush();
                lobbyRepository.save(openLobbys);
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
                .url(urlOfGameSession + "/info/started/" + id)
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
