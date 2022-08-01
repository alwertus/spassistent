package com.alwertus.spassistent.parts.info.service;

import com.alwertus.spassistent.parts.info.model.SpaceAccessEnum;
import com.alwertus.spassistent.parts.info.model.InfoUserOptions;
import com.alwertus.spassistent.parts.info.model.Space;
import com.alwertus.spassistent.parts.info.model.SpaceAccess;
import com.alwertus.spassistent.parts.info.repo.InfoUserOptionsRepository;
import com.alwertus.spassistent.parts.info.repo.SpaceAccessRepository;
import com.alwertus.spassistent.parts.info.repo.SpaceRepository;
import com.alwertus.spassistent.parts.info.dto.request.CreateSpaceRq;
import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class SpaceService {

    private static final String ERROR_SPACE = "Space is not determine";
    private static final String ERROR_ACCESS = "User can't access to modify this space";

    private final UserService userService;
    private final SpaceRepository spaceRepository;
    private final SpaceAccessRepository spaceAccessRepository;
    private final InfoUserOptionsRepository infoUserOptionsRepository;

    public List<Space> getSpaces() {
        User user = userService.getCurrentUser();
        return spaceAccessRepository
                .findAllByUser(user)
                .stream()
                .map(SpaceAccess::getSpace)
                .collect(Collectors.toList());
    }

    public void createSpace(CreateSpaceRq rq) {
        User currentUser = userService.getCurrentUser();

        Space space = new Space();
        space.setTitle(rq.getTitle());
        space.setDescription(rq.getDescription());
        space.setCreated(new Date());
        space.setCreatedBy(currentUser);
        spaceRepository.save(space);

        SpaceAccess spaceAccess = new SpaceAccess();
        spaceAccess.setUser(currentUser);
        spaceAccess.setSpace(space);
        spaceAccess.setAccess(SpaceAccessEnum.OWNER);
        spaceAccessRepository.save(spaceAccess);

        if (getCurrentSpace().isEmpty())
            selectSpace(space.getId());
    }

    public InfoUserOptions getInfoUserOptions() {
        return getUserOptions(userService.getCurrentUser());
    }

    public InfoUserOptions getUserOptions(User user) {
        StringBuilder sLog = new StringBuilder("getInfoUserOptions. ");

        Optional<InfoUserOptions> options = infoUserOptionsRepository
                .findByUser(user);

        if (options.isEmpty()) {
            sLog.append("UserOptions is null. Create new!");
            InfoUserOptions userOptions = new InfoUserOptions();
            userOptions.setUser(user);
            infoUserOptionsRepository.save(userOptions);
            log.trace(sLog);
            return userOptions;
        }

        sLog.append("Get");
        log.trace(sLog);
        return options.get();
    }

    public Optional<Space> getCurrentSpace() {
        log.trace("getCurrentSpace");
        Space space = getInfoUserOptions().getSelectedSpace();
        return space == null
                ? Optional.empty()
                : Optional.of(space);
    }

    public void selectSpace(Long id) {
        Space space = spaceRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Space '%s' not found", id)));
        /*User currentUser = userService.getCurrentUser();
        InfoUserOptions userOptions = infoUserOptionsRepository.findByUser(currentUser).orElse(new InfoUserOptions());
        userOptions.setUser(currentUser);*/
        InfoUserOptions userOptions = getInfoUserOptions();
        userOptions.setSelectedSpace(space);
        infoUserOptionsRepository.save(userOptions);
    }

    public void changeField(String field, String newValue) {
        Space space = getCurrentSpace().orElseThrow(() -> new RuntimeException(ERROR_SPACE));

        if (!isCurrentUserCanWrite())
            throw new RuntimeException(ERROR_ACCESS);

        switch (field) {
            case "title" -> space.setTitle(newValue);
            case "description" -> space.setDescription(newValue);
            default -> throw new RuntimeException("Try to change field '" + field + "'. Unknown field name");
        }

        spaceRepository.save(space);
    }

    public List<SpaceAccess> getAccessList(Space space) {
        return spaceAccessRepository.findAllBySpace(space);
    }

    public SpaceAccess getAccessForUser(Space space, User user) {
        return spaceAccessRepository
                .findAllBySpace(space)
                .stream()
                .filter(e -> e.getUser().equals(user))
                .findFirst()
                .orElse(null);
    }

    public boolean isCurrentUserCanWrite() {
        return checkUserCanWrite(
                getAccessForUser(
                        getCurrentSpace()
                                .orElseThrow(() -> new RuntimeException(ERROR_SPACE)),
                        userService.getCurrentUser()));
    }

    private boolean checkUserCanWrite(SpaceAccess access) {
        if (access == null) return false;

        return  access.getAccess().equals(SpaceAccessEnum.OWNER) ||
                access.getAccess().equals(SpaceAccessEnum.RW);
    }

    public void addUserToCurrentSpace(String userLogin) {
        User addedUser = userService.getUser(userLogin);
        Space currentSpace = getInfoUserOptions().getSelectedSpace();

        // Check if space determine
        if (currentSpace == null)
            throw new RuntimeException(ERROR_SPACE);

        // Check if current user has access to modify current space
        if (!checkUserCanWrite(getAccessForUser(currentSpace, userService.getCurrentUser())))
            throw new RuntimeException(ERROR_ACCESS);

        // Check added user not exists in access list for current space
        if (getAccessForUser(currentSpace, addedUser) != null)
            throw new RuntimeException("User already in access list");

        // Add User to Space
        SpaceAccess newSpaceAccess = new SpaceAccess();
        newSpaceAccess.setUser(addedUser);
        newSpaceAccess.setSpace(currentSpace);
        newSpaceAccess.setAccess(SpaceAccessEnum.RO);

        log.trace("Add userId='" + addedUser.getId()
                + "' to spaceId='" + currentSpace.getId()
                + "' with default access='" + newSpaceAccess.getAccess().toString() + "'");

        spaceAccessRepository.save(newSpaceAccess);

        InfoUserOptions addedUserOptions = getUserOptions(addedUser);
        if (addedUserOptions.getSelectedSpace() == null) {
            addedUserOptions.setSelectedSpace(newSpaceAccess.getSpace());
            infoUserOptionsRepository.save(addedUserOptions);
        }
    }

    public void changeUserAccess(Long userId, SpaceAccessEnum newAccess) {
        Space currentSpace = getInfoUserOptions().getSelectedSpace();

        // Check if space determine
        if (currentSpace == null)
            throw new RuntimeException(ERROR_SPACE);

        // Check if current user has access to modify current space
        if (!checkUserCanWrite(getAccessForUser(currentSpace, userService.getCurrentUser())))
            throw new RuntimeException(ERROR_ACCESS);

        // Check changed user exists in access list for current space
        SpaceAccess access = getAccessForUser(currentSpace, userService.getUser(userId));
        if (access == null)
            throw new RuntimeException("User is not in the access list");

        access.setAccess(newAccess);

        spaceAccessRepository.save(access);
    }
}
