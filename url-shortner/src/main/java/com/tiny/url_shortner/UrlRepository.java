package com.tiny.url_shortner;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UrlRepository extends JpaRepository<UrlMappingEntity, Long> {

    Optional<UrlMappingEntity> findByShortUrl(String url);

    @Query(value = "SELECT nextval('url_sequence')", nativeQuery = true)
    Long nextSequence();

}