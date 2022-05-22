package com.alwertus.spassistent.parts.info.view.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UploadImageRq {
    private String base64content;
    private String extension;
    private Long pageId;
}
