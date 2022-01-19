package com.alwertus.spassistent.parts.info.repo;

import com.alwertus.spassistent.parts.info.model.SpaceAccess;
import com.alwertus.spassistent.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceAccessRepository extends JpaRepository<SpaceAccess, Long> {
    List<SpaceAccess> findAllByUser(User user);
}
