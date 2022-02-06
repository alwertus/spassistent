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

@Log4j2
@Service
@RequiredArgsConstructor
public class PageService {
    private final PageRepository pageRepository;
    private final InfoService infoService;

    public void createPage(Long parentId, String title) {
        Space space = infoService.getCurrentSpace();
        if (space == null) {
            throw new RuntimeException("Cannot create a new page with no Space select");
        }

        Page page = new Page();
        page.setTitle(title);
        page.setSpace(space);

        if (parentId != null) {
            page.setParent(pageRepository.findById(parentId).orElse(null));
        }

        pageRepository.save(page);

    }

    public List<Page> getAvaliablePages() {
        Space space = infoService.getCurrentSpace();
        if (space == null) {
            log.debug("No space select. Return empty list");
            return Collections.emptyList();
        }
        log.debug("Find pagelist from spqce: " + space.getId());

        return pageRepository.findAllBySpace(space);
    }

    public String getHtml(Long id) {
        Space space = infoService.getCurrentSpace();

        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        if (page.getSpace() != space)
            throw new RuntimeException("Page not found");

        return page.getHtml();
    }

    public void saveHtml(Long id, String html) {
        Space space = infoService.getCurrentSpace();
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
