package com.self.interceptor;

import com.self.excptions.MyCustomException;
import com.self.utils.IPUtil;
import com.self.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.self.config.GlobalConstant.MOBILE_SMSCODE;
import static com.self.grace.result.ResponseStatusEnum.SMS_NEED_WAIT_ERROR;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/3
 */
@Slf4j
@Component
public class SMSInterceptor implements HandlerInterceptor {
    @Resource
    private RedisOperator redisOperator;
    /**
     * 拦截请求 在controller调用之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取用户的ip
        String userIP = IPUtil.getRequestIp(request);
        //获得用于判断是否存在的boolean
        boolean isExist = redisOperator.keyIsExist(MOBILE_SMSCODE + ":" + userIP);
        if(isExist){
            log.error("短信发送频率过高，稍后再试");
            throw new MyCustomException(SMS_NEED_WAIT_ERROR);//必须是抛异常，return接口无返回
        }
        // false 拦截  true 通过
        return true;
    }

    /**
     * 请求controller之后 渲染视图之前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 请求controller之后 渲染视图之后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
