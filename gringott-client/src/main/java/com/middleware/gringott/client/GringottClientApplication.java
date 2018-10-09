package com.middleware.gringott.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class GringottClientApplication {


    public static void main(String[] args) {
        /*SpringApplication.headless(false)
                .web(false).run(GringottClientApplication.class, args);*/

        new SpringApplicationBuilder(GringottClientApplication.class)
                .headless(false)
                .run(args);


    }
}
