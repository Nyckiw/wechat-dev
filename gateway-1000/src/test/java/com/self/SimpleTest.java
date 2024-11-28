package com.self;

import com.self.utils.RedisOperator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/11/27
 */
@SpringBootTest
public class SimpleTest {
    @Resource
    private RedisOperator redisOperator;
    @Test
    public void test01(){
        long limitLeftTimes = redisOperator.ttl("pppppp");
        System.out.println(limitLeftTimes);
    }
}
