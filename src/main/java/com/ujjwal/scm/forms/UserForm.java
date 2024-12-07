package com.ujjwal.scm.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserForm {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String about;
}
