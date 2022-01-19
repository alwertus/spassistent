package com.alwertus.spassistent.parts.info.view;

import com.alwertus.spassistent.common.view.ResponseOk;
import lombok.Getter;

import java.util.List;

public class SpacesListResponse extends ResponseOk {

    @Getter
    private final List<SpaceResponse> spaces;

    public SpacesListResponse(List<SpaceResponse> spaces) {
        this.spaces = spaces;
    }

}
