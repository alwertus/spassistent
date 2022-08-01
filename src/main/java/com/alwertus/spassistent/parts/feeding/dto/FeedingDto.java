package com.alwertus.spassistent.parts.feeding.dto;

import com.alwertus.spassistent.parts.feeding.model.Feeding;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class FeedingDto {

    private final Feeding feeding;

    public Long getId() {
        return feeding.getId();
    }

    public Long getStart() {
        if (feeding.getStart() == null)
            return null;

        return feeding.getStart().getTimeInMillis();
    }

    public Long getStop() {
        if (feeding.getStop() == null)
            return null;

        return feeding.getStop().getTimeInMillis();
    }

    public String getBreast() {
        return feeding.getBreast();
    }
}
