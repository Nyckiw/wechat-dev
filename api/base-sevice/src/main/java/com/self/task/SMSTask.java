package com.self.task;

import com.self.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/3
 */
@Slf4j
@Component
public class SMSTask {
    @Resource
    private SMSUtils smsUtils;

    @Async
    public void sendSMSInTask(String mobile, String code) {
        smsUtils.sendSMS(mobile, code);
        log.info("异步任务重所发送的验证码为：{}", code);
    }
}
