package com.alwertus.spassistent.parts.info.view.request;

import com.alwertus.spassistent.parts.info.model.SpaceAccessEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeUserAccessRq {
    Long userId;
    SpaceAccessEnum newAccess;
}
