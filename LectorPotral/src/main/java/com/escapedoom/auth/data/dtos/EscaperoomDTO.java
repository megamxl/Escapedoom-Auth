package com.escapedoom.auth.data.dtos;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class EscaperoomDTO {

    private Long UserId;

    private Long escaproom_id;

    private String name;

    private String topic;

    private long time;

    public EscaperoomDTO(Escaperoom escaperoom) {
        this.escaproom_id = escaperoom.getEscaperoom_id();
        this.UserId = escaperoom.getUser().getUser_id();
        this.time = escaperoom.getTime();
        this.topic = escaperoom.getTopic();
        this.name = escaperoom.getName();
    }
}
