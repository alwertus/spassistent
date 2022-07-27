package com.alwertus.spassistent.parts.info.controller;

import com.alwertus.spassistent.common.service.FileService;
import com.alwertus.spassistent.common.view.Response;
import com.alwertus.spassistent.common.view.ResponseError;
import com.alwertus.spassistent.common.view.ResponseOk;
import com.alwertus.spassistent.parts.info.model.Page;
import com.alwertus.spassistent.parts.info.model.Space;
import com.alwertus.spassistent.parts.info.service.PageService;
import com.alwertus.spassistent.parts.info.service.SpaceService;
import com.alwertus.spassistent.parts.info.view.request.*;
import com.alwertus.spassistent.parts.info.view.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/info")
public class InfoController {
    private final SpaceService spaceService;
    private final PageService pageService;
    private final FileService fileService;

    @PostMapping("/getSpaces")
    public Response getSpaces() {
        log.info("Get spaces");

        List<SpaceRs> spaces = spaceService
                .getSpaces()
                .stream().map(SpaceRs::new)
                .collect(Collectors.toList());

        Space selectedSpace = spaceService
                .getInfoUserOptions()
                .getSelectedSpace();

        return new SpacesListRs(
                spaces,
                selectedSpace == null ? null : selectedSpace.getId()
        );
    }

    @PostMapping("/createSpace")
    public Response createSpace(@RequestBody CreateSpaceRq rq) {
        log.info("Create new Space");
        spaceService.createSpace(rq);
        return new ResponseOk();
    }

    @PostMapping("/getCurrentSpaceInfo")
    public Response getCurrentSpaceInfo() {
        Optional<Space> space = spaceService.getCurrentSpace();
        if (space.isEmpty())
            throw new RuntimeException("Space is not determine");
        return new GetCurrentSpaceInfoRs(space.get(), spaceService.getAccessList(space.get()), spaceService.isCurrentUserCanWrite());
    }

    @PostMapping("/changeSpaceInfo")
    public Response changeSpaceInfo(@RequestBody ChangeSpaceInfoRq rq) {
        if (rq.getField() == null || rq.getField().isEmpty())
            throw new RuntimeException("Request 'field' is null");
        spaceService.changeField(rq.getField(), rq.getNewValue());
        return new ResponseOk();
    }

    @PostMapping("/selectSpace")
    public Response selectSpace(@RequestBody SelectSpaceRq rq) {
        log.info("Select InfoSpace id=" + rq.getSpaceId());
        spaceService.selectSpace(rq.getSpaceId());
        return new ResponseOk();
    }

    @PostMapping("/addUserToSpace")
    public Response addLoginToSpace(@RequestBody AddLoginToSpaceRq rq) {
        spaceService.addUserToCurrentSpace(rq.getLogin());
        return new ResponseOk();
    }
    @PostMapping("/changeUserAccess")
    public Response changeUserAccess(@RequestBody ChangeUserAccessRq rq) {
        log.info("Change user access to: " + rq);
        spaceService.changeUserAccess(rq.getUserId(), rq.getNewAccess());
        return new ResponseOk();
    }

    @PostMapping("/createPage")
    public Response createPage(@RequestBody CreatePageRq rq) {
        log.info("Create page title=" + rq.getTitle());
        pageService.createPage(rq.getParentId(), rq.getTitle());
        return new ResponseOk();
    }
    @PostMapping("/movePage")
    public Response movePage(@RequestBody MovePageRq rq) {
        if (rq.getFrom() == null)
            return new ResponseError("From is null");

        log.info("Move page id=" + rq.getFrom() + " to new parent id=" + (rq.getTo() == null ? "null" : rq.getTo()));
        pageService.movePage(rq.getFrom(), rq.getTo());
        return new ResponseOk();
    }

    @PostMapping("/renamePage")
    public Response renamePage(@RequestBody RenamePageRq rq) {
        log.info("Rename page id=" + rq.getId() + " newTitle=" + rq.getNewTitle());
        pageService.renamePage(rq.getId(), rq.getNewTitle());
        return new ResponseOk();
    }

    @PostMapping("/getPageList")
    public Response getPageList() {
        log.info("GetPageList");
        List<Page> availablePages = pageService.getAvailablePages();
        return new PagesListRs(getChildRecursive(null, availablePages));
    }

    private List<PageRs> getChildRecursive(Long parentId, List<Page> findList) {
       return findList.stream()
                .filter(e -> e.getParent() == null ? parentId == null : e.getParent().getId().equals(parentId))
                .map(e -> {
                    PageRs pr = new PageRs(e);
                    pr.setChildList(getChildRecursive(e.getId(), findList));
                    return pr;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/getHtml")
    public Response getHtml(@RequestBody GetPageRq rq) {
        return new GetPageRs(pageService.getPage(rq.getId()));
    }

    @PostMapping("/saveHtml")
    public Response saveHtml(@RequestBody SaveHtmlRq rq) {
        pageService.saveHtml(rq.getId(), rq.getHtml());
        return new ResponseOk();
    }

    @PostMapping("/uploadImage")
    public Response uploadImage(@RequestBody UploadImageRq rq) {
        log.info("Upload image");

        return new UploadImageRs(fileService.saveFile(rq.getPageId(), rq.getBase64content(), rq.getExtension()));
    }

    /*@GetMapping("/getImage/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName, HttpServletRequest rq) {
        // load file as Resource
        Resource resource = fileService.getFile(fileName);
        String contentType = "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }*/

    @ExceptionHandler(Exception.class)
    public Response errorHandler(Exception e) {
        log.error("ErrorHandler: " + e.getMessage());
        return new ResponseError(e.getMessage());
    }

}
