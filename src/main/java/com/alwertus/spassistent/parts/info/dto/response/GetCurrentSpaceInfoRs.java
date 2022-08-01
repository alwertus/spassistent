package com.alwertus.spassistent.parts.info.dto.response;

import com.alwertus.spassistent.common.dto.ResponseOk;
import com.alwertus.spassistent.parts.info.model.Space;
import com.alwertus.spassistent.parts.info.model.SpaceAccess;
import com.alwertus.spassistent.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@SuppressWarnings("unused")
@RequiredArgsConstructor
public class GetCurrentSpaceInfoRs extends ResponseOk {
    private final Space space;
    private final List<SpaceAccess> accessList;

    @Getter
    private final boolean currentUserCanWrite;

    public String getTitle ()  {
        return space.getTitle();
    }

    public String getDescription() {
        return space.getDescription();
    }

    public List<AccessListRq> getAccessList() {
        return accessList
                .stream()
                .map(AccessListRq::new)
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    public static class AccessListRq {
        private final SpaceAccess spaceAccess;

        public Long getUserId() {
            return spaceAccess.getUser().getId();
        }

//      disable for security
//        public String getUserLogin() {
//            return spaceAccess.getUser().getLogin();
//        }

        public String getAccess() {
            return spaceAccess.getAccess().toString();
        }

        public String getUserName() {
            User user = spaceAccess.getUser();
            if (user.getFirstName() == null && user.getLastName() == null)
                return user.getLogin();
            return String.join(" ", user.getFirstName(), user.getLastName());
        }

    }
}
