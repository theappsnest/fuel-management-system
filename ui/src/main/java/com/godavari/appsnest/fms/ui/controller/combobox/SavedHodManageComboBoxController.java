package com.godavari.appsnest.fms.ui.controller.combobox;

import com.godavari.appsnest.fms.dao.model.HodManage;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.HodManageRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.HodManageSelectionEvent;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXComboBox;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.util.StringConverter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Log4j
public class SavedHodManageComboBoxController extends BaseComboBoxController<HodManage> {

    private static final String LOG_TAG = SavedHodManageComboBoxController.class.getSimpleName();

    @FXML
    private JFXComboBox<HodManage> jfxComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");
        MyEventBus.register(this);
        jfxComboBox.setConverter(this);
        jfxComboBox.valueProperty().addListener(this);
        jfxComboBox.setEditable(true);
        refreshList();
    }

    private void refreshList() {
        log.info("refreshList");
        try {
            jfxComboBox.getItems().setAll(HodManage.getAllByCurrent(true));
            new AutoCompletionTextFieldBinding(jfxComboBox.getEditor(),
                    SuggestionProvider.create(jfxComboBox.getItems()), this);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public String toString(HodManage hodManage) {
        if (hodManage != null) {
            return hodManage.getDepartment().getName() + Utility.TEXT_CONNECTOR + hodManage.getHeadOfDepartment().getName();
        }
        return null;
    }

    @Override
    public HodManage fromString(String text) {
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(text.trim())) {
            return null;
        }

        FilteredList<HodManage> filteredList = jfxComboBox.getItems().filtered(new Predicate<HodManage>() {
            @Override
            public boolean test(HodManage hodManage) {
                String[] textSplit = text.split(Utility.TEXT_CONNECTOR);
                if (textSplit.length > 0 && hodManage.getDepartment().getName().equals(textSplit[0])) {
                    return true;
                }
                return false;
            }
        });
        return filteredList.get(0);
    }

    @Override
    public void changed(ObservableValue<? extends HodManage> observableValue, HodManage oldValue, HodManage newValue) {
        if (newValue != null) {
            log.info("changed: " + newValue);
            MyEventBus.post(new HodManageSelectionEvent(newValue));
        }
    }

    @Subscribe
    protected void refreshList(HodManageRefreshEvent departmentRefreshEvent) {
        log.info("refreshList");
        if (departmentRefreshEvent != null) {
            jfxComboBox.getSelectionModel().clearSelection();
            refreshList();
        }
    }

    @Subscribe
    protected void selectHodManage(HodManageSelectionEvent hodManageSelectionEvent) {
        log.info("selectHodManage");
        if (hodManageSelectionEvent != null) {
            jfxComboBox.getSelectionModel().select(hodManageSelectionEvent.getHodManage());
        }
    }
}
