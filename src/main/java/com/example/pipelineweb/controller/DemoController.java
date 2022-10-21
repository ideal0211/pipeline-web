package com.example.pipelineweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {

    @GetMapping("/hi")
    public String hi() {
        return "cleveland,this is for you!";
    }

}
