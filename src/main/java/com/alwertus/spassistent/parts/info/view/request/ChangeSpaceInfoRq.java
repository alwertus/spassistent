package com.alwertus.spassistent.parts.info.view.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeSpaceInfoRq {
    String field;
    String newValue;
}
