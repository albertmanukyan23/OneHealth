package com.example.onehealthmvc.aspect;

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
public class ControllerLoggingAspect {

    @Pointcut("execution(* com.example.onehealthmvc.controller.*.*(..))")
    public void logEndpointMethods() {
    }

    @Before("logEndpointMethods()")
    public void logBeforeEndpointCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Calling controller: " + methodName);
    }

    @AfterReturning(pointcut = "logEndpointMethods()", returning = "result")
    public void logAfterEndpointCall(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Controller " + methodName + " executed successfully. Result: " + result);
    }
}
