package com.escapedoom.auth.Service;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import com.escapedoom.auth.data.dataclasses.repositories.EscaperoomRepository;
import com.escapedoom.auth.data.dataclasses.repositories.UserRepository;
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

    public void createADummyRoom() {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Escaperoom dummy =
                Escaperoom.builder().user((User) user).build();
        escaperoomRepository.save(dummy);
    }

    public List<EscaperoomDTO> getAllRoomsByAnUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var rooms = escaperoomRepository.findEscaperoomByUser(user).orElseThrow();
        List<EscaperoomDTO> ret =new ArrayList<>();
        rooms.stream().forEach(curr -> ret.add(new EscaperoomDTO(curr)));
        return ret;
    }


}
