package com.alwertus.spassistent.parts.info.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveHtmlRequest {
    Long id;
    String html;
}
