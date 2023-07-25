package com.example.assignment;

import com.example.assignment.price.Price;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;

@SpringBootApplication
public class AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
        Price price = new Price();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int time = (int) (timestamp.getTime() / 5000);
        price.getPrice(time);
        price.getPrice(time + 5);
        price.getPrice(time + 20);
    }

}
