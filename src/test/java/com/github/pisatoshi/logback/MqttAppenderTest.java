package com.github.pisatoshi.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttAppenderTest {
    static final Logger logger = LoggerFactory.getLogger(MqttAppenderTest.class);
  
    public static void main(String[] args) {
        logger.info("Entering application.");
        logger.info("Exiting application.");
        System.exit(0);
    }
}
