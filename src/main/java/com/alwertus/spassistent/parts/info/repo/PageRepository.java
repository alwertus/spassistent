package com.alwertus.spassistent.parts.info.repo;

import com.alwertus.spassistent.parts.info.model.Page;
import com.alwertus.spassistent.parts.info.model.Space;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PageRepository extends CrudRepository<Page, Long> {
    List<Page> findAllBySpace(Space space);
}
