package com.alwertus.spassistent.parts.info.view.response;

import com.alwertus.spassistent.common.view.ResponseOk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PagesListRs extends ResponseOk {

    @Getter
    private final List<PageRs> pages;
}
