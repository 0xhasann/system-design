package com.redis.rate.limiter;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RateLimiterFactory {

    private final Map<RateLimitType, RateLimitStrategy> strategies;

    public RateLimiterFactory(
            FixedWindowStrategy fixed,
            SlidingWindowStrategy sliding,
            TokenBucketStrategy token) {

        strategies = Map.of(
                RateLimitType.FIXED, fixed,
                RateLimitType.SLIDING, sliding,
                RateLimitType.TOKEN, token);
    }

    public RateLimitStrategy get(RateLimitType type) {

        return strategies.get(type);

    }

}
