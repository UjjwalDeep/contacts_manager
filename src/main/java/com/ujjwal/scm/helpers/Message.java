package com.ujjwal.scm.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private String content;

    @Builder.Default
    private MessageType type = MessageType.blue;
}
