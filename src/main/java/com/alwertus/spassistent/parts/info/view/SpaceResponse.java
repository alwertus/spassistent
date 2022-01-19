package com.alwertus.spassistent.parts.info.view;

import com.alwertus.spassistent.parts.info.model.Space;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class SpaceResponse {
    private final Space space;
    public Long getId() { return space.getId(); }

    public String getTitle() {
        return space.getTitle();
    }

    public String getDescription() {
        return space.getDescription();
    }

    public String getCreatedBy() {
        return space.getCreatedBy() != null ? space.getCreatedBy().getLogin() : null;
    }

    public Date getCreated() {
        return space.getCreated();
    }
}
