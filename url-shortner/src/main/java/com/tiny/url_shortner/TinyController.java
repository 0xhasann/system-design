package com.tiny.url_shortner;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class TinyController {

    private final TinyService service;

    private final UrlRepository repository;

    @PostMapping("/createTinyUrl/{longUrl}")
    public ResponseEntity<String> createTinyUrl(@PathVariable String longUrl) {
        String shortUrl = service.encode7(longUrl);

        return ResponseEntity.status(201).body(shortUrl);
    }

    @GetMapping("getLongUrl/{url}")
    public ResponseEntity<String> getMethodName(@PathVariable("url") String url) {
        System.out.println("GET called with: " + url);

        UrlMappingEntity entity = repository.findByShortUrl(url).orElseThrow();
        System.out.println(entity);
        return ResponseEntity.ok(entity.getLongUrl());
    }

}
