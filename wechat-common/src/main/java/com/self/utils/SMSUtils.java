package com.self.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SMSUtils {


    public void sendSMS(String phone, String code) {

        log.info("SMSUtils方法已发送验证码");
    }


}


