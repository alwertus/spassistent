package com.alwertus.spassistent.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ResponseOkText extends ResponseOk {

    @Getter
    private final String text;

}
