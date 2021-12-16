package ru.sfedu.servicestation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Main {
    private static Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        log.debug("AppForRunnerClient[0]: starting application...");
        log.info(Arrays.toString(args));
    }

    public void logBasicSystemInfo() {
        log.info("Launching the application...");
        log.info("Operating System: " + System.getProperty("os.name") + " " +
                System.getProperty("os.version"));
        log.info("JRE: " + System.getProperty("java.version"));
        log.info("Java Launched From: " + System.getProperty("java.home"));
        log.info("Class Path: " + System.getProperty("java.class.path"));
        log.info("Library Path: " + System.getProperty("java.library.path"));
        log.info("User Home Directory: " + System.getProperty("user.home"));
        log.info("User Working Directory: " + System.getProperty("user.dir"));
        log.debug("Test DEBUG logging.");
        log.info("Test INFO logging.");
        log.warn("Test WARN logging.");
        log.error("Test ERROR logging.");
    }
}
