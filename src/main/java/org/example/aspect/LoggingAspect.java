package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Measures and logs the execution time of the annotated method.
     *
     * @param joinPoint the proceeding join point providing access to the target method
     * @return the result of the target method invocation
     * @throws Throwable if the target method throws an exception, it is re-thrown
     */
    @Around("@annotation(org.example.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();
        long executionTime;
        try {
            Object result = joinPoint.proceed();
            executionTime = System.currentTimeMillis() - start;
            log.info("Method {} executed in {} ms", methodName, executionTime);
            return result;
        } catch (Throwable t) {
            executionTime = System.currentTimeMillis() - start;
            log.error("Execution of method {} was interrupted after {} ms", methodName, executionTime);
            throw t;
        }
    }
}