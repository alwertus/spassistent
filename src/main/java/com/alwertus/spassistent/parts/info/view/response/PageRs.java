package com.alwertus.spassistent.parts.info.view.response;

import com.alwertus.spassistent.parts.info.model.Page;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class PageRs {
    private final Page page;

    @Getter
    @Setter
    private List<PageRs> childList;

    public Long getId() {
        return page.getId();
    }

    public String getTitle() {
        return page.getTitle();
    }

    public Integer getPosition() { return page.getPosition(); }

    public Long getParentId() {
        if (page.getParent() == null)
            return null;

        return page.getParent().getId();
    }


    @Override
    public String toString() {
        return "PageResponse{" +
                "page=" + page +
                ", childList=" + childList +
                '}';
    }

}
