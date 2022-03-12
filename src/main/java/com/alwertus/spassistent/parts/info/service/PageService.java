package com.alwertus.spassistent.parts.info.service;

import com.alwertus.spassistent.parts.info.model.Page;
import com.alwertus.spassistent.parts.info.model.Space;
import com.alwertus.spassistent.parts.info.repo.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class PageService {
    private final PageRepository pageRepository;
    private final SpaceService spaceService;

//TODO: во всяких методах добавить проверку, что пользователь имеет право менять значения spaceService.isCurrentUserCanWrite()

    private Space getCurrentSpace(String errorText) {
        return spaceService
                .getCurrentSpace()
                .orElseThrow(() -> new RuntimeException(errorText));
    }

    public void createPage(Long parentId, String title) {
        Space space = getCurrentSpace("Cannot create a new page with no Space select");

        Page page = new Page();
        page.setTitle(title);
        page.setSpace(space);

        if (parentId != null) {
            page.setParent(pageRepository.findById(parentId).orElse(null));
        }

        pageRepository.save(page);
    }

    public void movePage(Long fromId, Long toId) {
        Space space = getCurrentSpace("Cannot create a new page with no Space select");

        Page page = pageRepository.findById(fromId).orElseThrow(() -> new RuntimeException("Page id=" + fromId + " not found"));
        if (page.getSpace() != space)
            throw new RuntimeException("Wrong space (page id='" + fromId + "'). Operation is now allowed");

        if (toId == null) {
            page.setParent(null);
        } else {
            Page newParent = pageRepository.findById(toId).orElseThrow(() -> new RuntimeException("Page id=" + toId + " not found"));
            if (newParent.getSpace() != space)
                throw new RuntimeException("Wrong space (page id='" + toId + "'). Operation is now allowed");
            page.setParent(newParent);
        }

        pageRepository.save(page);
    }

    public List<Page> getAvailablePages() {
        Optional<Space> space = spaceService.getCurrentSpace();

        if (space.isEmpty()) {
            log.trace("getAvailablePages. No space select. Return empty list");
            return Collections.emptyList();
        }
        log.trace("getAvailablePages. Find pagelist from space: " + space.get().getId());

        return pageRepository.findAllBySpace(space.get());
    }

    public Page getPage(Long id) {
        Space space = getCurrentSpace("Space is not determine");

        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        if (page.getSpace() != space)
            throw new RuntimeException("Cannot access page id=" + id + " from space id=" + space.getId());

        return page;
    }

    public void renamePage(Long id, String newTitle) {
        Space space = getCurrentSpace("Space is not determine");

        Page page = pageRepository.findByIdAndSpace(id, space)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        page.setTitle(newTitle);

        pageRepository.save(page);
    }

    public void saveHtml(Long id, String html) {
        Space space = getCurrentSpace("Space is not determine");
        ArrayList<String> arr = new ArrayList<>();
        if (space == null) arr.add("space");
        if (id == null) arr.add("id");
        if (html == null) arr.add("html");
        if (arr.size() > 0)
            throw new RuntimeException("Null parameter: " + String.join(", ", arr));

        Page page = pageRepository.findById(id).orElseThrow(() -> new RuntimeException("Page not found"));

        page.setHtml(html);
        pageRepository.save(page);
    }
}
