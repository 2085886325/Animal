package com.project.animal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstTest {
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }


}
