package com.example.Library.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /** ✅ Log method calls */
    @Before("execution(* com.example.Library.service.*.*(..))")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        logger.info("Method called: " + joinPoint.getSignature().toShortString());
    }

    /** ✅ Log execution time */
    @Around("execution(* com.example.Library.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("Execution time of {}: {} ms", joinPoint.getSignature().toShortString(), elapsedTime);
        return result;
    }

    /** ✅ Log exceptions */
    @AfterThrowing(pointcut = "execution(* com.example.Library.service.*.*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in {}: {}", joinPoint.getSignature().toShortString(), exception.getMessage());
    }
}
