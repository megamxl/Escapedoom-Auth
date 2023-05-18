package com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class EscapeRoomDTO {

    @Id
    @GeneratedValue
    private Long id;

    private String staringTime;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Scenes> stage;

}
