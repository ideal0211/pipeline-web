package com.example.pipelineweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PipelineWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PipelineWebApplication.class, args);
    }

}
