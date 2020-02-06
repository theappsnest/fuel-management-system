package com.godavari.appsnest.fms.ui.frame;

import com.godavari.appsnest.fms.ui.utility.ResourceString;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.PropertyConfigurator;

@Log4j
public abstract class BaseApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        //PropertiesConfigurator is used to configure logger from properties file
        PropertyConfigurator.configure(getClass().getClassLoader().getResource("logger_property/log4j.properties"));

        //Log in console in and log file
        log.info("Log4j appender configuration is successful, Application started");

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(getFxmlPath()), ResourceString.getResourceBundle());
        stage.setScene(new Scene(root));
        stage.show();
    }

    public abstract String getFxmlPath();
}
