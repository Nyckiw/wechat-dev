package com.self.utils;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/10
 */
public class SnowUtils {

    public static Long getId(){
        SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker(0L);
        return snowflakeIdWorker.nextId();
    }
}
