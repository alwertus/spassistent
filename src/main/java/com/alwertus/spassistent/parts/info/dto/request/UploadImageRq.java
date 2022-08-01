package com.alwertus.spassistent.parts.info.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UploadImageRq {
    private String base64content;
    private String extension;
    private Long pageId;
}
