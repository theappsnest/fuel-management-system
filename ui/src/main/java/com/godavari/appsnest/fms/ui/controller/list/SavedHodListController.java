package com.godavari.appsnest.fms.ui.controller.list;

import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.HodRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.HodSelectionEvent;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class SavedHodListController extends BaseListController<HeadOfDepartment> {

    private static final String LOG_TAG = SavedHodListController.class.getSimpleName();

    @FXML
    private JFXListView<HeadOfDepartment> jfxListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");
        MyEventBus.register(this);
        jfxListView.setCellFactory(this);
        jfxListView.getSelectionModel().selectedItemProperty().addListener(this);
        refreshSavedHodList();
    }

    private void refreshSavedHodList() {
        try {
            jfxListView.getItems().setAll(HeadOfDepartment.getAll());
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public ListCell<HeadOfDepartment> call(ListView<HeadOfDepartment> headOfDepartmentListView) {
        return new ListCell<HeadOfDepartment>() {
            @Override
            protected void updateItem(HeadOfDepartment headOfDepartment, boolean empty) {
                super.updateItem(headOfDepartment, empty);
                if (empty || headOfDepartment == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(headOfDepartment.getName());
                }
            }
        };
    }

    @Override
    public void changed(ObservableValue<? extends HeadOfDepartment> observableValue, HeadOfDepartment oldValue, HeadOfDepartment newValue) {
        if (newValue != null) {
            log.info("changed: " + newValue);
            MyEventBus.post(new HodSelectionEvent(newValue));
        }
    }

    @Subscribe
    protected void refreshList(HodRefreshEvent hodRefreshEvent) {
        if (hodRefreshEvent != null) {
            refreshSavedHodList();
        }
    }
}
