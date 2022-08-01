package com.alwertus.spassistent.parts.feeding.service;

import com.alwertus.spassistent.common.service.RandomStringGenerator;
import com.alwertus.spassistent.parts.feeding.model.Feeding;
import com.alwertus.spassistent.parts.feeding.model.FeedingProperties;
import com.alwertus.spassistent.parts.feeding.model.FeedingUserOptions;
import com.alwertus.spassistent.parts.feeding.repo.FeedingOptionsRepository;
import com.alwertus.spassistent.parts.feeding.repo.FeedingPropertiesRepository;
import com.alwertus.spassistent.parts.feeding.repo.FeedingRepository;
import com.alwertus.spassistent.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class FeedingService {

    private final FeedingOptionsRepository feedingOptionsRepository;
    private final UserService userService;
    private final RandomStringGenerator randomStringGenerator;
    private final FeedingRepository feedingRepository;
    private final FeedingPropertiesRepository feedingPropertiesRepository;

    private Optional<FeedingUserOptions> getCurrentUserOptions() {
        return feedingOptionsRepository
                .findByUserId(userService.getCurrentUser().getId());
    }

    private String getAccessIdOrThrowError() {
        return getCurrentUserOptions()
                .orElseThrow(() -> new RuntimeException("Cannot get current user AccessId"))
                .getAccessId();
    }

    public boolean isUserGetAccess() {
        Optional<FeedingUserOptions> userOptions = getCurrentUserOptions();

        return userOptions.isPresent()
                && userOptions.get().getAccessId() != null
                && !userOptions.get().getAccessId().isEmpty();
    }

    public void createNewAccess() {
        FeedingProperties props = new FeedingProperties();
        props.setId(randomStringGenerator.generateString(10));
        feedingPropertiesRepository.save(props);

        FeedingUserOptions newUserOptions = new FeedingUserOptions();
        newUserOptions.setUser(userService.getCurrentUser());
        newUserOptions.setAccessId(props.getId());
        feedingOptionsRepository.save(newUserOptions);
    }

    public void addAccess(@NonNull String accessId) {
        FeedingUserOptions newUserOptions = new FeedingUserOptions();
        newUserOptions.setUser(userService.getCurrentUser());
        newUserOptions.setAccessId(accessId);
        feedingOptionsRepository.save(newUserOptions);
    }

    public List<Feeding> getData() {
        Optional<FeedingUserOptions> userOptions = getCurrentUserOptions();
        if (userOptions.isEmpty())
            return Collections.emptyList();

        return feedingRepository.findFirst20ByAccessIdOrderByStartDesc(userOptions.get().getAccessId());
    }

    public Long getActiveTimer() {
        return feedingRepository.getLastTimer(getAccessIdOrThrowError());
    }

    public String getInterval() {
        FeedingProperties props = feedingPropertiesRepository
                .findById(getAccessIdOrThrowError())
                .orElseThrow(() -> new RuntimeException("Interval not exists"));
        return String.format("%02d", props.getIntervalHour()) + ":" + String.format("%02d", props.getIntervalMin());

    }

    public void newTimer(String breast) {
        FeedingProperties props = feedingPropertiesRepository
                .findById(getAccessIdOrThrowError())
                .orElseThrow(() -> new RuntimeException("Interval not exists"));

        Feeding feeding = new Feeding();

        feeding.setStart(Calendar.getInstance());

        Calendar stop = Calendar.getInstance();
        stop.add(Calendar.HOUR_OF_DAY, props.getIntervalHour());
        stop.add(Calendar.MINUTE, props.getIntervalMin());
        feeding.setStop(stop);

        feeding.setAccessId(getAccessIdOrThrowError());

        if (breast.length() > 0)
            feeding.setBreast(breast.substring(0, 1));

        log.debug("Add new timer " + feeding);
        feedingRepository.save(feeding);
    }

    public void newInterval(int hour, int min) {
        FeedingProperties props = feedingPropertiesRepository
                .findById(getAccessIdOrThrowError())
                .orElseThrow(() -> new RuntimeException("Interval not exists"));
        props.setIntervalHour(hour);
        props.setIntervalMin(min);

        feedingPropertiesRepository.save(props);
    }

}
