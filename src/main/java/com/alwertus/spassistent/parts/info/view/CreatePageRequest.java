package com.alwertus.spassistent.parts.info.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatePageRequest {
    String title;
    Long parentId;
}
