package com.artezio.forms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.artezio"})
public class CamundaWebapp {
    public static void main(String... args) {
        SpringApplication.run(CamundaWebapp.class, args);
    }
}
