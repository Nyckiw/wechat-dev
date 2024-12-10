package com.self;

import com.self.utils.IPUtil;
import com.self.utils.SnowUtils;
import org.junit.jupiter.api.Test;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/10
 */
public class SimpleTest {
    @Test
    public void test01(){
        Long id = SnowUtils.getId();
        System.out.println(id);
    }
    @Test
    public void test02(){
//        String userIP = IPUtil.getRequestIp(request);
    }
}
