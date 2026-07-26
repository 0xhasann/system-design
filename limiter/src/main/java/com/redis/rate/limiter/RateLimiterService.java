package com.redis.rate.limiter;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RateLimiterFactory factory;

    // public RateLimiterService(RateLimitStrategy strategy) {
    // this.strategy = strategy;
    // }

    public boolean allow(String user,
            RateLimitType type) {

        return factory.get(type).allowRequest(user);
    }

}
