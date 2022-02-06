package com.alwertus.spassistent.parts.info.view;

import com.alwertus.spassistent.common.view.ResponseOk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PagesListResponse extends ResponseOk {

    @Getter
    private final List<PageResponse> pages;
}
