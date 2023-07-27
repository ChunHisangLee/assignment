package com.example.assignment;

import com.example.assignment.entity.Price;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;

@SpringBootApplication
@MapperScan("com.example.assignment.mapper")
public class AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
        Price price = new Price();
        Instant instant = Instant.now();
        long seconds = instant.getEpochSecond() / 5;
        price.getPrice(seconds);
        price.getPrice(seconds + 5);
        price.getPrice(seconds + 20);
    }

}
