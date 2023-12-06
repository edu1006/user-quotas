package com.vicarius.quotas.service.impl;

import com.vicarius.quotas.expections.QuotaExceededException;
import com.vicarius.quotas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class RateLimiter {
    private static final Integer maxRequests = 5;
    private final Map<String, Integer> requestCounts = new HashMap<>();

    public boolean allowRequest(String userId) {
        int count = requestCounts.getOrDefault(userId, 0);
        if (count < maxRequests) {
            requestCounts.put(userId, count + 1);
            return Boolean.TRUE;
        }
        throw new QuotaExceededException("Quota exceeded for user "+userId);
    }

    public void resetCount(String userId) {
        requestCounts.put(userId, 0);
    }
}
