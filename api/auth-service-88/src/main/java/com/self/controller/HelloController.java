package com.self.controller;

import com.self.task.SMSTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author jcy
 * @version 1.0
 * @data 2024/11/28
 */
@RequestMapping("/a")
@RestController
@Slf4j
public class HelloController {
    @Resource
    private SMSTask smsTask;
    @GetMapping("/hello")
    public String testAuth(){
        return "auth-service hello";
    }
    @GetMapping("/sms")
    public void testSMS(){
        smsTask.sendSMSInTask("16788900765","9999");
        log.info("运行完成");
    }
}
