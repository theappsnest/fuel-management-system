package com.godavari.appsnest.fms.ui.controller;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.ui.eventbus.Subscriber;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public abstract class BaseFrameController<T> implements Initializable, EventHandler<ActionEvent>, IOnActionPerformed, Subscriber {

    private static final String LOG_TAG = BaseFrameController.class.getSimpleName();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    protected abstract void updateUi(T object);

    protected abstract void populateObject();

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        log.info("onActionPerformedResult, resultMessage: " + resultMessage);
        //todo add handling for exception generic
    }
}
