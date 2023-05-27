package com.escapedoom.gamesession.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "escape_room_stage")
public class EscapeRoomDao {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "escape_roomid")
    private Long roomId;

    @Column(name = "stage_id")
    private Long stageId;

    @Column(name = "stage")
    private String stage;

    @Override
    public String toString() {
        return "EscapeRoomStage{" +
                "id=" + id +
                ", stage=" + stage +
                '}';
    }
}
