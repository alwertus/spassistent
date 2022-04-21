package com.alwertus.spassistent.parts.info.view.response;

import com.alwertus.spassistent.common.view.ResponseOk;
import com.alwertus.spassistent.parts.info.model.Page;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class GetPageRs extends ResponseOk {
    private final Page page;

    public String getHtml() {
        return page.getHtml();
    }

    public String getTitle() {
        return page.getTitle();
    }

    public Boolean getIsFavorite() { return page.getIsFavorite() != null && page.getIsFavorite(); }
}
