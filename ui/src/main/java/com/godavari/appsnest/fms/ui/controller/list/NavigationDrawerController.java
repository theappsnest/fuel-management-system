package com.godavari.appsnest.fms.ui.controller.list;

import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.NavigationDrawerSelectionEvent;
import com.godavari.appsnest.fms.ui.frame.AccountFrame;
import com.godavari.appsnest.fms.ui.frame.tab.TabHodManageDepartmentFrame;
import com.godavari.appsnest.fms.ui.frame.tab.TabReportsFrame;
import com.godavari.appsnest.fms.ui.frame.tab.TabVehicleAssignedToDepartmentFrame;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class NavigationDrawerController implements Initializable, EventHandler<ActionEvent> {
    @FXML
    private ImageView ivImage;

    @FXML
    private JFXButton bJfxHodManage;

    @FXML
    private JFXButton bJfxVehicleAssigned;

    @FXML
    private JFXButton bJfxAccount;

    @FXML
    private JFXButton bJfxReports;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("initialize");

        bJfxHodManage.setOnAction(this);
        bJfxVehicleAssigned.setOnAction(this);
        bJfxAccount.setOnAction(this);
        bJfxReports.setOnAction(this);

        ivImage.requestFocus();
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        log.info("handle, actionEvent: " + actionEvent.getSource());
        String url = null;
        if (actionEvent.getSource() == bJfxHodManage) {
            url = TabHodManageDepartmentFrame.URL_NEW;
        } else if (actionEvent.getSource() == bJfxVehicleAssigned) {
            url = TabVehicleAssignedToDepartmentFrame.URL_NEW;
        } else if (actionEvent.getSource() == bJfxAccount) {
            url = AccountFrame.URL_NEW;
        } else if (actionEvent.getSource() == bJfxReports) {
            url = TabReportsFrame.URL_NEW;
        }
        MyEventBus.post(new NavigationDrawerSelectionEvent(url));
    }
}
