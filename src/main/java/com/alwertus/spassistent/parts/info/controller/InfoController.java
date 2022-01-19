package com.alwertus.spassistent.parts.info.controller;

import com.alwertus.spassistent.common.view.Response;
import com.alwertus.spassistent.common.view.ResponseError;
import com.alwertus.spassistent.common.view.ResponseOk;
import com.alwertus.spassistent.parts.info.model.InfoUserOptions;
import com.alwertus.spassistent.parts.info.service.IInfoService;
import com.alwertus.spassistent.parts.info.view.CreateSpaceRequest;
import com.alwertus.spassistent.parts.info.view.SelectSpaceRequest;
import com.alwertus.spassistent.parts.info.view.SpaceResponse;
import com.alwertus.spassistent.parts.info.view.SpacesListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/info")
public class InfoController {
    private final IInfoService infoService;

    @PostMapping("/getSpaces")
    public SpacesListResponse getSpaces() {
        log.info("Get spaces");

        List<SpaceResponse> spaces = infoService
                .getSpaces()
                .stream().map(SpaceResponse::new)
                .collect(Collectors.toList());

        InfoUserOptions userOptions = infoService.getInfoUserOptions();
        Long selectedSpace = null;
        if (userOptions != null)
            selectedSpace = userOptions.getSelectedSpace().getId();

        return new SpacesListResponse(spaces, selectedSpace);
    }

    @PostMapping("/createSpace")
    public Response createSpace(@RequestBody CreateSpaceRequest rq) {
        log.info("Create new Space");
        try {
            infoService.createSpace(rq);
            return new ResponseOk();
        } catch (Exception e) {
            return new ResponseError(e.getMessage());
        }
    }

    @PostMapping("/selectSpace")
    public Response selectSpace(@RequestBody SelectSpaceRequest rq) {
        log.info("Select InfoSpace id=" + rq.getSpaceId());
        try {
            infoService.selectSpace(rq.getSpaceId());
            return new ResponseOk();
        } catch (Exception e) {
            return new ResponseError(e.getMessage());
        }
    }


}
