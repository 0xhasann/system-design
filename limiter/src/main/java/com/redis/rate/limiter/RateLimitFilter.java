package com.redis.rate.limiter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;

    private final RateLimiterResolver limiterResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {

        String user = request.getHeader("X-USER-ID");

        System.out.println("doFilterInternal");

        RateLimitType type = limiterResolver.resolve(request);

        if (!rateLimiterService.allow(user, type)) {

            response.setStatus(429);
            response.getWriter().write("Too Many Requests");
            System.out.println(response);
            return;
        }

        filterChain.doFilter(request, response);

    }

}
