package com.godavari.appsnest.fms;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        //PropertiesConfigurator is used to configure logger from properties file
        PropertyConfigurator.configure(Main.class.getClassLoader().getResource("logger_property/log4j.properties"));

        //Log in console in and log file
        logger.info("Log4j appender configuration is successful, Application started");

        // Launching the Start Screen Ui
        //Application.launch(StartScreenFrame.class, args);
    }
}