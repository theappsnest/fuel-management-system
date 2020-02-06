package com.godavari.appsnest.fms.ui.controller.list;

import com.godavari.appsnest.fms.dao.model.HodManage;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.HodManageRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.HodManageSelectionEvent;
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
public class SavedHodManageListController extends BaseListController<HodManage>{

    private static final String LOG_TAG = SavedHodManageListController.class.getSimpleName();

    @FXML
    private JFXListView<HodManage> jfxListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");
        MyEventBus.register(this);
        jfxListView.setCellFactory(this);
        jfxListView.getSelectionModel().selectedItemProperty().addListener(this);
        refreshSavedList();
    }

    private void refreshSavedList() {
        try {
            jfxListView.getItems().setAll(HodManage.getAllByCurrent(true));
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public ListCell<HodManage> call(ListView<HodManage> headOfDepartmentListView) {
        return new ListCell<HodManage>() {
            @Override
            protected void updateItem(HodManage hodManage, boolean empty) {
                super.updateItem(hodManage, empty);
                if (empty || hodManage == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(hodManage.getDepartment().getName());
                }
            }
        };
    }

    @Override
    public void changed(ObservableValue<? extends HodManage> observableValue, HodManage oldValue, HodManage newValue) {
        if (newValue != null) {
            log.info("changed: " + newValue);
            MyEventBus.post(new HodManageSelectionEvent(newValue));
        }
    }

    @Subscribe
    protected void refreshList(HodManageRefreshEvent hodRefreshEvent) {
        if (hodRefreshEvent != null) {
            refreshSavedList();
        }
    }
}
