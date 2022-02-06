package com.alwertus.spassistent.parts.info.view;

import com.alwertus.spassistent.common.view.ResponseOk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HtmlResponse extends ResponseOk {

    @Getter
    private final String html;
}
