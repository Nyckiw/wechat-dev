package com.self.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/12
 */
@Component
@Slf4j
@Aspect
public class ServiceLogAspect {
    @Around("execution(* com.self.controller..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //需要统计每一个service 的实现执行时间 长时间的记录错误日志
        StopWatch stopWatch = new StopWatch();
        //类名.方法名
        String pointName = joinPoint.getTarget().getClass().getSimpleName()
                +"."
                +joinPoint.getSignature().getName();
        stopWatch.start("执行业务："+pointName);
        Object proceed = joinPoint.proceed();
        stopWatch.stop();


        //日志打印
        log.info("***********************************");
        log.info(stopWatch.prettyPrint());//详细日志打印
        log.info("***********************************");
        log.info(stopWatch.shortSummary());//简短日志打印
        log.info("任务总数："+stopWatch.getTaskCount());
        log.info("任务总执行时间"+stopWatch.getTotalTimeMillis()+"ms");

        //记录慢执行
        long takeTimes = stopWatch.getTotalTimeMillis();
        if (takeTimes > 3000) {
            log.error("执行位置{}，执行时间太长了，耗费了{}毫秒", pointName, takeTimes);
        } else if (takeTimes > 2000) {
            log.warn("执行位置{}，执行时间稍微有点长，耗费了{}毫秒", pointName, takeTimes);
        } else {
            log.info("执行位置{}，执行时间正常，耗费了{}毫秒", pointName, takeTimes);
        }



        return proceed;
    }
}
