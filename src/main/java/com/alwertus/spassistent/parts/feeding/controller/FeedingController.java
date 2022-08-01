package com.alwertus.spassistent.parts.feeding.controller;

import com.alwertus.spassistent.common.dto.Response;
import com.alwertus.spassistent.common.dto.ResponseOk;
import com.alwertus.spassistent.common.dto.ResponseOkText;
import com.alwertus.spassistent.parts.feeding.dto.*;
import com.alwertus.spassistent.parts.feeding.service.FeedingService;
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
@RequestMapping("/api/feeding")
public class FeedingController {

    private final FeedingService feedingService;

    @PostMapping("/getAccessStatus")
    public Response getAccessStatus() {

        return new ResponseOkText(String.valueOf(feedingService.isUserGetAccess()));
    }

    @PostMapping("/createAccess")
    public Response createAccess() {
        if (!feedingService.isUserGetAccess()) {
            feedingService.createNewAccess();
        }

        return new ResponseOk();
    }

    @PostMapping("/addAccess")
    public Response addAccess(@RequestBody AddAccessRq rq) {
        if (!feedingService.isUserGetAccess()) {
            feedingService.addAccess(rq.getAccessId());
        }

        return new ResponseOk();
    }

    @PostMapping("/addTimer")
    public Response addTimer(@RequestBody AddTimerRq rq) {
        feedingService.newTimer(rq.getBreast());

        return new ResponseOk();
    }

    @PostMapping("/getData")
    public Response getData() {

        List<FeedingDto> feedingDtoList = feedingService
                .getData()
                .stream()
                .map(FeedingDto::new)
                .collect(Collectors.toList());

        return new GetDataRs(feedingDtoList, feedingService.getActiveTimer(), feedingService.getInterval());
    }

    @PostMapping("/setNewInterval")
    public Response setNewInterval(@RequestBody SetNewIntervalRq rq) {
        feedingService.newInterval(rq.getHour(), rq.getMin());

        return new ResponseOk();
    }


}
