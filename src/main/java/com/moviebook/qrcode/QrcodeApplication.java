package com.moviebook.qrcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@ComponentScan
@Configuration
public class QrcodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrcodeApplication.class, args);
    }

}
