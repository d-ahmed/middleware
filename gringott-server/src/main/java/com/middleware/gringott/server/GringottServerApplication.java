package com.middleware.gringott.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class GringottServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GringottServerApplication.class, args);

        /*new SpringApplicationBuilder(GringottServerApplication.class)
                .headless(false)
                .run(args);*/
    }
}
