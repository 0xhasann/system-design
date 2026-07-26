package com.redis.rate.limiter;

import java.time.Instant;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlidingWindowStrategy implements RateLimitStrategy {

    private final RedisRateLimitRepository repository;

    private static final int LIMIT = 100;

    private static final long WINDOW = 60;

    @Override
    public boolean allowRequest(String user) {

        String key = "sliding:" + user;

        long now = Instant.now().getEpochSecond();

        long cutoff = now - WINDOW;

        // Remove expired requests
        repository.removeOlderThan(key, cutoff);

        Long count = repository.count(key);

        if (count >= LIMIT) {
            return false;
        }

        repository.addTimestamp(key, now);

        return true;
    }

}
