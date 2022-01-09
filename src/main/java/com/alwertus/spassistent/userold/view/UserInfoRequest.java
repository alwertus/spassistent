package com.alwertus.spassistent.userold.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoRequest {
    private String Login;
    private String Password;
    private String FirstName;
    private String LastName;
}
