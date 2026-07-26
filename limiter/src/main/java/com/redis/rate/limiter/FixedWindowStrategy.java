package com.redis.rate.limiter;

import java.time.Duration;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FixedWindowStrategy implements RateLimitStrategy {

    private final RedisRateLimitRepository repository;

    private static final int LIMIT = 5;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    @Override
    public boolean allowRequest(String userId) {

        String key = "rate:" + userId;

        Long count = repository.increment(key);

        System.out.println("key :: " + key + " count :: " + count);

        if (count == 1) {

            repository.expire(key, WINDOW);

        }

        return count <= LIMIT;
    }

}