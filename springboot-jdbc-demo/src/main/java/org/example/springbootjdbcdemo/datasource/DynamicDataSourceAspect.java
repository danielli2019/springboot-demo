package org.example.springbootjdbcdemo.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DynamicDataSourceAspect {
    @Before("execution(* org.example.springbootjdbcdemo.service.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        // set business key as required
        String businessKey = determinBusinessKey(joinPoint);
        BusinessContextHolder.setBusinessKey(businessKey);
    }

    @After("execution(* org.example.springbootjdbcdemo.service.*.*(..))")
    public void afterMethod(JoinPoint joinPoint) {
        // clear business key context
        BusinessContextHolder.clearBusinessKey();
    }

    private String determinBusinessKey(JoinPoint joinPoint) {
        return "businessDataSource1";
    }
}
