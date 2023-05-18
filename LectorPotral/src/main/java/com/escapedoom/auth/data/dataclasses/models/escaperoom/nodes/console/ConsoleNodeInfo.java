package com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsoleNodeInfo extends NodeInfoAbstract {

    private long outputID;

    private String codeSnipped;

    private String desc;

    private String png;

    private String title;

}
