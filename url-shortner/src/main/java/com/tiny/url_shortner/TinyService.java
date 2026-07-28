package com.tiny.url_shortner;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TinyService {

    private final UrlRepository repository;

    private final CounterService counterService;

    public String encode7(String url) {

        log.info("url = {}", url);

        // long id = repository.nextSequence();

        // fetch next 1000 sequences

        long id = counterService.next();
        String encoded = Base62.encode(id);

        String shortUrl = "0".repeat(7 - encoded.length()) + encoded;

        log.info(shortUrl);

        UrlMappingEntity entity = UrlMappingEntity.builder().id(id).shortUrl(shortUrl).longUrl(url)
                .createdAt(LocalDateTime.now())
                .build();

        log.info(entity.toString());

        repository.save(entity);

        return shortUrl;

    }

}
