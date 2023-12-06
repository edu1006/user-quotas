package com.vicarius.quotas.aspects;

import com.vicarius.quotas.expections.QuotaExceededException;
import com.vicarius.quotas.model.User;
import com.vicarius.quotas.service.UserService;
import com.vicarius.quotas.service.impl.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class QuotaAspect {

    @Autowired
    private RateLimiter rateLimiter;

    @Autowired
    private UserService userService;

    @Around("@annotation(com.vicarius.quotas.constraints.QuotaLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = (String) joinPoint.getArgs()[0];
        //validate if users exists
        User user = userService.getUserById(userId);
        if (!rateLimiter.allowRequest(userId)) {
            throw new QuotaExceededException("User quota exceeded");
        }
        user.setQuota(user.getQuota() + 1);
        user.setLastLoginTimeUtc(LocalDateTime.now());
        this.userService.updateUser(user, userId);
        return joinPoint.proceed();
    }

}
