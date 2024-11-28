package com.self.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/11/28
 */
@RestController
@RequestMapping("/m")
public class HelloController {
    @GetMapping("/hello")
    public String testMain(){
        return "main-service hello";
    }
}
