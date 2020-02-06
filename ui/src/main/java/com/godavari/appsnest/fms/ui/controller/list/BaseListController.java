package com.godavari.appsnest.fms.ui.controller.list;

import com.godavari.appsnest.fms.ui.eventbus.Subscriber;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseListController<T> implements Initializable, Callback<ListView<T>, ListCell<T>>, ChangeListener<T>, Subscriber {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
