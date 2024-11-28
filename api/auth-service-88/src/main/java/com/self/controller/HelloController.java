package com.self.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/11/28
 */
@RestController
public class HelloController {
    @GetMapping("/a/hello")
    public String testAuth(){
        return "auth-service hello";
    }
}
