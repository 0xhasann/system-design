package com.redis.rate.limiter;

import java.time.Duration;
import java.util.Map;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRateLimitRepository {

    private final StringRedisTemplate redisTemplate;

    public Long increment(String key) {

        return redisTemplate.opsForValue().increment(key);

    }

    public void expire(String key, Duration ttl) {

        redisTemplate.expire(key, ttl);

    }

    public Long getTTL(String key) {

        return redisTemplate.getExpire(key);

    }

    public void addTimestamp(String key, long timestamp) {

        redisTemplate.opsForZSet()
                .add(key, String.valueOf(timestamp), timestamp);

    }

    public void removeOlderThan(String key, long cutoff) {

        redisTemplate.opsForZSet()
                .removeRangeByScore(key, 0, cutoff);

    }

    public Long count(String key) {

        return redisTemplate.opsForZSet()
                .zCard(key);

    }

    public Map<Object, Object> getHash(String key) {

        return redisTemplate.opsForHash().entries(key);

    }

    public void putHash(String key,
            Map<String, String> values) {

        redisTemplate.opsForHash().putAll(key, values);

    }

}
