package com.example.final_project.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(com.example.final_project.configuration.CustomLogger)")
    private void customLogger() {
    }

    @Around(value = "customLogger()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("method started: " + LocalDateTime.now());
        Object result = joinPoint.proceed();
        System.out.println("method ended: " + LocalDateTime.now());
        return result;
    }
}
