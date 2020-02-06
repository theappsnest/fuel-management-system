package com.godavari.appsnest.fms.ui.controller.combobox;

import com.godavari.appsnest.fms.ui.eventbus.Postable;
import com.godavari.appsnest.fms.ui.eventbus.Subscriber;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.util.StringConverter;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public abstract class BaseComboBoxController<T> extends StringConverter<T> implements Initializable, ChangeListener<T>, Postable, Subscriber {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
