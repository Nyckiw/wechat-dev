package com.self.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/11/28
 */
@RestController
public class HelloController {
    @GetMapping("/f/hello")
    public String testFile(){
        return "file-service hello";
    }
}
