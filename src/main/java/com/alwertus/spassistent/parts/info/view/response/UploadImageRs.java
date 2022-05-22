package com.alwertus.spassistent.parts.info.view.response;

import com.alwertus.spassistent.common.view.ResponseOk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UploadImageRs extends ResponseOk {

    @Getter
    private final String url;
}
