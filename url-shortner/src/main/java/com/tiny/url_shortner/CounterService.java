package com.tiny.url_shortner;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class CounterService {

    private final UrlRepository repository;

    AtomicLong current;
    long max;

    synchronized void loadNextRange() {
        long start = repository.allocate(1000);

        current.set(start);
        max = start + 999;
    }

    long next() {

        long value = current.incrementAndGet();

        if (value > max) {
            loadNextRange();
            value = current.incrementAndGet();
        }

        return value;
    }
}