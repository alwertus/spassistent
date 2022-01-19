package com.alwertus.spassistent.parts.info.service;

import com.alwertus.spassistent.parts.info.model.Space;
import com.alwertus.spassistent.parts.info.model.SpaceAccess;
import com.alwertus.spassistent.parts.info.repo.SpaceAccessRepository;
import com.alwertus.spassistent.parts.info.repo.SpaceRepository;
import com.alwertus.spassistent.parts.info.view.CreateSpaceRequest;
import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InfoService implements IInfoService {
    private final UserService userService;
    private final SpaceRepository spaceRepository;
    private final SpaceAccessRepository spaceAccessRepository;

    @Override
    public List<Space> getSpaces() {
        User user = userService.getCurrentUser();
        return spaceAccessRepository
                .findAllByUser(user)
                .stream()
                .map(SpaceAccess::getSpace)
                .collect(Collectors.toList());
    }

    @Override
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
}