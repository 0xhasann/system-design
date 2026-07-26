package com.redis.rate.limiter;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class RateLimiterResolver {

    public RateLimitType resolve(HttpServletRequest request) {

        String path = request.getRequestURI();

        if (path.startsWith("/login")) {
            return RateLimitType.FIXED;
        } else if (path.startsWith(("/payment"))) {
            return RateLimitType.TOKEN;
        }

        return RateLimitType.SLIDING;
    }

}
