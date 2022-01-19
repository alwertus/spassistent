package com.alwertus.spassistent.parts.info.repo;

import com.alwertus.spassistent.parts.info.model.InfoUserOptions;
import com.alwertus.spassistent.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InfoUserOptionsRepository extends CrudRepository<InfoUserOptions, Long> {
    Optional<InfoUserOptions> findByUser(User user);
}
