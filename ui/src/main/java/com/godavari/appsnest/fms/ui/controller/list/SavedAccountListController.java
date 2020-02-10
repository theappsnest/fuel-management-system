package com.godavari.appsnest.fms.ui.controller.list;

import com.godavari.appsnest.fms.dao.model.*;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.AccountRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.AccountSelectionEvent;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class SavedAccountListController extends BaseListController<Account> {

    private static final String LOG_TAG = SavedAccountListController.class.getSimpleName();

    @FXML
    private JFXListView<Account> jfxListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");
        MyEventBus.register(this);
        jfxListView.setCellFactory(this);
        jfxListView.getSelectionModel().selectedItemProperty().addListener(this);
        refreshSavedAccountList();
    }

    private void refreshSavedAccountList() {
        try {
            jfxListView.getItems().setAll(Account.getAll());
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public ListCell<Account> call(ListView<Account> accountListView) {
        return new ListCell<Account>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                if (empty || account == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HodManage hodManage = account.getHodManage();
                    HeadOfDepartment headOfDepartment = hodManage.getHeadOfDepartment();
                    Department department = hodManage.getDepartment();
                    Vehicle vehicle = null;
                    if (account.getVehicleAssigned() != null){// && account.getVehicleAssigned().getId()>0) {
                        VehicleAssigned vehicleAssigned = account.getVehicleAssigned();
                        vehicle = vehicleAssigned.getVehicle();
                    }

                    StringBuilder stringBuilder = new StringBuilder(account.getId());
                    stringBuilder.append(account.getDateTime().toLocalDate())
                            .append(Utility.TEXT_CONNECTOR + account.getOutput())
                            .append(Utility.TEXT_CONNECTOR + department.getName());
                    if (vehicle != null) {
                        stringBuilder.append(Utility.TEXT_CONNECTOR + vehicle.getVehicleNo());
                    }

                    setText(stringBuilder.toString());
                }
            }
        };
    }

    @Override
    public void changed(ObservableValue<? extends Account> observableValue, Account oldValue, Account newValue) {
        if (newValue != null) {
            log.info("changed: " + newValue);
            MyEventBus.post(new AccountSelectionEvent(newValue));
        }
    }

    @Subscribe
    protected void refreshList(AccountRefreshEvent accountRefreshEvent) {
        if (accountRefreshEvent != null) {
            refreshSavedAccountList();
        }
    }
}
