package com.alwertus.spassistent.parts.info.dto.response;

import com.alwertus.spassistent.common.dto.ResponseOk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@RequiredArgsConstructor
public class SpacesListRs extends ResponseOk {

    @Getter
    private final List<SpaceRs> spaces;

    @Getter
    @Nullable
    private final Long selectedSpace;

}
