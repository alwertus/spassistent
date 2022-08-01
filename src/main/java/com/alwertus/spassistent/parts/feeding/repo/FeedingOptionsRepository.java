package com.alwertus.spassistent.parts.feeding.repo;

import com.alwertus.spassistent.parts.feeding.model.FeedingUserOptions;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FeedingOptionsRepository extends CrudRepository<FeedingUserOptions, Long> {

    Optional<FeedingUserOptions> findByUserId(Long userId);

}