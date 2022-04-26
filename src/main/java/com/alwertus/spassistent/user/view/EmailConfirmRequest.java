package com.alwertus.spassistent.user.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailConfirmRequest {
    private String confirmString;
}