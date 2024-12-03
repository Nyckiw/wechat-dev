package com.self.controller;

import com.self.grace.result.GraceJSONResult;
import com.self.task.SMSTask;
import com.self.utils.IPUtil;
import com.self.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.self.config.GlobalConstant.MOBILE_SMSCODE;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/3
 */
@RequestMapping("/passport")
@RestController
@Slf4j
public class PassportController {
    @Resource
    private RedisOperator redisOperator;
    @Resource
    private SMSTask smsTask;

    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {
        //安全校验
        if (StringUtils.isBlank(mobile)) {
            return GraceJSONResult.error();
        }
        //获取用户的ip（手机号）
        String userIp = IPUtil.getRequestIp(request);
        //限制该用户的ip在60s内只能获取一次验证码[语句仅是不重复的基础上存入key,具体的防刷在拦截器]
        redisOperator.setnx60s(MOBILE_SMSCODE + ":" + userIp, mobile);

        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        smsTask.sendSMSInTask(mobile, code);
        //将验证码存redis 用于后续注册、登录的校验
        redisOperator.set(MOBILE_SMSCODE + ":" + mobile, code, 10 * 60);
        return GraceJSONResult.ok();
    }
}
