package com.almor.kafkauserlistener.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserMessage {

    private String id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

}
