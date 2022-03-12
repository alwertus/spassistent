package com.alwertus.spassistent.parts.info.view.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class MovePageRq {
    Long from;

    @Nullable
    Long to;
}
