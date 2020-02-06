package com.godavari.appsnest.fms.ui.controller.custom;

import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.CurrentReadingModel;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.Subscriber;
import com.godavari.appsnest.fms.ui.eventbus.event.AccountRefreshEvent;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class CurrentReadingController implements Initializable, Subscriber {

    @FXML
    private Label lCurrentReading;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("initialize");

        MyEventBus.register(this);
        refresh();
    }

    private void refresh() {
        try {
            lCurrentReading.setText(String.valueOf(CurrentReadingModel.getCurrentStockReading()));
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Subscribe
    public void refreshAccount(AccountRefreshEvent accountRefreshEvent) {
        if (accountRefreshEvent != null) {
            refresh();
        }
    }
}
