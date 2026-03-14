package app.auth.service.Security;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class ResilienceRateLimiter {

    // Map key → user/IP
    private final ConcurrentMap<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    private RateLimiter createRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(100) // max 100 requests
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(0))
                .build();
        return RateLimiter.of("default", config);
    }

    public RateLimiter getRateLimiter(String key) {
        return limiters.computeIfAbsent(key, k -> createRateLimiter());
    }
}
