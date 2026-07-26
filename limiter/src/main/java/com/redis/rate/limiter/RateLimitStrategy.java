package com.redis.rate.limiter;

public interface RateLimitStrategy {

    boolean allowRequest(String key);

}
