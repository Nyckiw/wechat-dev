package com.self.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
@Getter
public enum PlatformEnum {

    TOKEN_APP_PREFIX( "app");


    public final String type;

    PlatformEnum(String type) {
        this.type = type;

    }


}
