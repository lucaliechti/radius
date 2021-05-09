package radius.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @AfterReturning(pointcut="execution(* radius.web.controller.*.* (..))", returning="result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        log.debug(joinPoint.getSignature().getName() + " was called. Return value: " + result);
    }

}
