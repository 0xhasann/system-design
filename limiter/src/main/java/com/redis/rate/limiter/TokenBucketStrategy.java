package com.redis.rate.limiter;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBucketStrategy implements RateLimitStrategy {

    private final RedisRateLimitRepository repository;

    private static final int CAPACITY = 10;

    private static final int REFILL_RATE = 1; // token/sec

    @Override
    public boolean allowRequest(String user) {

        String key = "bucket:" + user;

        long now = Instant.now().getEpochSecond();

        Map<Object, Object> bucket = repository.getHash(key);

        double tokens;
        long lastRefill;

        if (bucket.isEmpty()) {

            tokens = CAPACITY;
            lastRefill = now;

        } else {

            tokens = Double.parseDouble(
                    bucket.get("tokens").toString());

            lastRefill = Long.parseLong(
                    bucket.get("lastRefill").toString());

        }

        long elapsed = now - lastRefill;

        tokens = Math.min(
                CAPACITY,
                tokens + elapsed * REFILL_RATE);

        if (tokens < 1) {

            repository.putHash(key, Map.of(
                    "tokens", String.valueOf(tokens),
                    "lastRefill", String.valueOf(now)));

            return false;
        }

        tokens--;

        repository.putHash(key, Map.of(
                "tokens", String.valueOf(tokens),
                "lastRefill", String.valueOf(now)));

        return true;
    }

}
