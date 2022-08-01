package com.alwertus.spassistent.parts.info.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RenamePageRq {
    Long id;
    String newTitle;
}
