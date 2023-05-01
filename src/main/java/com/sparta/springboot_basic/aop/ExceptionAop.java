package com.sparta.springboot_basic.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionAop {

    @Around("execution(public * com.sparta.springboot_basic.controller..*(..))")
    public synchronized Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            // 핵심기능 수행
            Object output = joinPoint.proceed();
            return output;
        } finally {

        }
    }
}
