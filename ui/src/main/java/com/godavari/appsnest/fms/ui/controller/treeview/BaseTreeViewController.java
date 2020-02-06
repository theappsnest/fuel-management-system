package com.godavari.appsnest.fms.ui.controller.treeview;

import com.godavari.appsnest.fms.ui.eventbus.Subscriber;
import com.google.common.eventbus.Subscribe;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public abstract class BaseTreeViewController<T> implements Initializable, ChangeListener<TreeItem<T>>, Callback<TreeView, TreeCell>, Subscriber {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
