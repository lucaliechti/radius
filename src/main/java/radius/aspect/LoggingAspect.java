package radius.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
public class LoggingAspect {

    /*
    @Before("execution(* radius.web.controller.*.* (..))")
    public void before(JoinPoint joinPoint) {
        System.out.print("Before ");
        System.out.println(joinPoint.getSignature().getName());
    }


    @AfterReturning("execution(* radius.web.controller.*.* (..))")
    public void afterReturning(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getName() + " returned");
    }*/

    @AfterReturning(pointcut="execution(* radius.web.controller.*.* (..))", returning="result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println(joinPoint.getSignature().getName() + " was called. Return value: " + result);
    }

}