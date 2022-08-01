package com.alwertus.spassistent.parts.info.dto.response;

import com.alwertus.spassistent.common.dto.ResponseOk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PagesListRs extends ResponseOk {

    @Getter
    private final List<PageRs> pages;
}
