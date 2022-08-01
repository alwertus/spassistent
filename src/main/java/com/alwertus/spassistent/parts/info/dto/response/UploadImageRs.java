package com.alwertus.spassistent.parts.info.dto.response;

import com.alwertus.spassistent.common.dto.ResponseOk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UploadImageRs extends ResponseOk {

    @Getter
    private final String url;
}
