package com.tiny.url_shortner;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface UrlRepository extends JpaRepository<UrlMappingEntity, Long> {

    Optional<UrlMappingEntity> findByShortUrl(String url);

    @Query(value = "SELECT nextval('url_sequence')", nativeQuery = true)
    Long nextSequence();

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE id_allocator
            SET next_id = next_id + :size
            WHERE name = 'url'
            RETURNING next_id - :size
            """, nativeQuery = true)
    long allocate(@Param("size") int size);

}