package com.alwertus.spassistent.parts.feeding.repo;

import com.alwertus.spassistent.parts.feeding.model.Feeding;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedingRepository extends CrudRepository<Feeding, Long> {

    List<Feeding> findFirst20ByAccessIdOrderByStartDesc(String accessId);

    @Query("""
            select t.id
            from Feeding t
            where
                t.accessId = :accessId
                and t.start = (
                    select max(s.start)
                    from Feeding s
                    where s.accessId = t.accessId
                        )
""")
    Long getLastTimer(@Param("accessId") String accessId);

}