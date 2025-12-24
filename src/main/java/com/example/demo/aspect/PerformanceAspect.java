package com.example.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {
  private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceAspect.class.getName());

  @Around("@annotation(com.example.demo.util.annotation.TrackTime)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    Object proceed = joinPoint.proceed();

    long executionTime = System.currentTimeMillis() - startTime;
    LOGGER.info("Method [{}] executed in: {} ms", joinPoint.getSignature(), executionTime);
    return proceed;
  }
}
