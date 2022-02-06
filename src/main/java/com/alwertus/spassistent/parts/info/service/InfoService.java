package com.alwertus.spassistent.parts.info.service;

import com.alwertus.spassistent.parts.info.model.InfoUserOptions;
import com.alwertus.spassistent.parts.info.model.Space;
import com.alwertus.spassistent.parts.info.model.SpaceAccess;
import com.alwertus.spassistent.parts.info.repo.InfoUserOptionsRepository;
import com.alwertus.spassistent.parts.info.repo.SpaceAccessRepository;
import com.alwertus.spassistent.parts.info.repo.SpaceRepository;
import com.alwertus.spassistent.parts.info.view.CreateSpaceRequest;
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
public class InfoService {
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

    public void createSpace(CreateSpaceRequest rq) {
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
        spaceAccess.setAccess("OWNER");
        spaceAccessRepository.save(spaceAccess);
    }

    public InfoUserOptions getInfoUserOptions() {
        User currentUser = userService.getCurrentUser();

        Optional<InfoUserOptions> options = infoUserOptionsRepository
                .findByUser(currentUser);

        if (options.isEmpty()) {
            log.trace("UserOptions is null. Create new!");
            InfoUserOptions userOptions = new InfoUserOptions();
            userOptions.setUser(currentUser);
            infoUserOptionsRepository.save(userOptions);
            return userOptions;
        }

        return options.get();
    }

    public Space getCurrentSpace() {
        return getInfoUserOptions().getSelectedSpace();
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
}
