package com.self.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RedisKeyEnum {

    REDIS_USER_TOKEN("redis_user_token:");

    private final String key;
}
