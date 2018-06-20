package com.sgt.student.service.gateway.logging;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * This class is used to log performance stats using Aspectj
 */

@Aspect
@Component
public class PerformanceLoggingAspect {

    private Logger logger = Logger.getLogger(PerformanceLoggingAspect.class.getName());

    /**
     * An Advice method that logs the time taking in millis seconds for a method to execute,
     * that is annotated with the <i>@LogExecutionTime</i> annotation.
     *
     * @param joinPoint the proceedingJoinPoint
     * @return an java.lang.Object
     * @throws Throwable if target method throws an exception during execution
     */
    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch(joinPoint.getSignature().getDeclaringTypeName());
        stopWatch.start();
        final Object proceed = joinPoint.proceed();
        stopWatch.stop();
        logger.info(joinPoint.getSignature().getDeclaringTypeName() + " executed in " + stopWatch.getTotalTimeMillis() + "ms");
        return proceed;
    }

}
