package com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataNodeInfo extends NodeInfoAbstract {

    private Long outputID;

    private String title;

    private String desc;

    private String parameterType;

    private String exampleOutput;

    private String codeSnipped;
}
