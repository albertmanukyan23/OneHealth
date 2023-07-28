package com.example.onehealthrest.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class EndpointLoggingAspect {

    @Pointcut("execution(* com.example.onehealthrest.endpoint.*.*(..))")
    public void logEndpointMethods() {
    }

    @Before("logEndpointMethods()")
    public void logBeforeEndpointCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Calling endpoint: " + methodName);
    }

    @AfterReturning(pointcut = "logEndpointMethods()", returning = "result")
    public void logAfterEndpointCall(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Endpoint " + methodName + " executed successfully. Result: " + result);
    }
}
