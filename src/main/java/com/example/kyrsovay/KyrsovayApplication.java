package com.example.kyrsovay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class KyrsovayApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(KyrsovayApplication.class, args);

    }

}
