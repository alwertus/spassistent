package com.alwertus.spassistent.parts.info.view;

import com.alwertus.spassistent.parts.info.model.Page;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PageResponse {
    private final Page page;

    public Long getId() {
        return page.getId();
    }

    public String getTitle() {
        return page.getTitle();
    }
}
