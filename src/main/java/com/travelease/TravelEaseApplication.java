package com.travelease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class TravelEaseApplication {

    private static final Logger logger = LoggerFactory.getLogger(TravelEaseApplication.class);

    public static void main(String[] args) {
        logger.info("Starting TravelEase Application...");
        SpringApplication.run(TravelEaseApplication.class, args);
        logger.info("TravelEase Application Started Successfully.");
    }
}
