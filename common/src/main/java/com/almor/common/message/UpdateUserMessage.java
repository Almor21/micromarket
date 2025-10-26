package com.almor.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserMessage {

    private String id;

    private String email;

    private String firstName;

    private String lastName;

    private Boolean enable;

}
