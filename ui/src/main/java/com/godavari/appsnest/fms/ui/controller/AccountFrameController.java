package com.godavari.appsnest.fms.ui.controller;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.listener.delete.DeleteAccountActionPerformed;
import com.godavari.appsnest.fms.core.listener.insert.InsertAccountActionPerformed;
import com.godavari.appsnest.fms.core.listener.update.UpdateAccountActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.HodManage;
import com.godavari.appsnest.fms.dao.model.VehicleAssigned;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.*;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.util.StringConverter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class AccountFrameController extends ChildBaseFrameController<Account> {

    @FXML
    private JFXDatePicker jfxDatePicker;

    @FXML
    private JFXTimePicker jfxTimePicker;

    @FXML
    private JFXTextField jfxTFInput;

    @FXML
    private JFXTextField jfxTFOutput;

    @FXML
    private JFXTextField jfxTFCurrentReading;

    @FXML
    private JFXTextField jfxTFOwner;

    private LocalDateTime dateTime;
    private HodManage hodManage;
    private VehicleAssigned vehicleAssigned;
    private double currentReading;
    private double input;
    private double output;
    private String owner;

    private Account account;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("initialize");
        super.initialize(url, resourceBundle);

        MyEventBus.register(this);

        lSavedListTitle.setText(resourceBundle.getString("saved_account_entry"));
        lFrameTitle.setText(resourceBundle.getString("account_string"));

        jfxTimePicker.set24HourView(true);

        jfxDatePicker.setValue(LocalDate.now());
        jfxTimePicker.setValue(LocalTime.now());

        jfxDatePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                String text = null;
                if (localDate != null) {
                    text = dateTimeFormatter.format(localDate);
                }
                return text;
            }

            @Override
            public LocalDate fromString(String text) {
                LocalDate localDate = null;
                if (!StringUtils.isEmpty(text) || !StringUtils.isEmpty(text.trim())) {
                    String[] textSplit = text.split("/");
                    if (textSplit.length == 2) {
                        localDate = LocalDate.of(LocalDate.now().getYear(),
                                Integer.valueOf(textSplit[1]), Integer.valueOf(textSplit[0]));
                    } else {
                        localDate = LocalDate.parse(text, dateTimeFormatter);
                    }
                }
                return localDate;
            }
        });

        jfxTimePicker.setConverter(new StringConverter<LocalTime>() {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalTime localTime) {
                String text = null;
                if (localTime != null) {
                    text = dateTimeFormatter.format(localTime);
                }
                return text;
            }

            @Override
            public LocalTime fromString(String text) {
                LocalTime localTime = null;
                if (text != null && !text.trim().isEmpty()) {
                    localTime = LocalTime.parse(text);
                }
                return localTime;
            }
        });
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        super.onActionPerformedResult(resultMessage);
        log.info(resultMessage);
        if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS) {
            switch (resultMessage.getResultCode()) {
                case SUCCESS_CODE_INSERT_ACCOUNT:
                case SUCCESS_CODE_DELETE_ACCOUNT:
                case SUCCESS_CODE_UPDATE_ACCOUNT:
                    MyEventBus.post(new AccountRefreshEvent());
                    updateUi(null);
                    Utility.showDialogBox(Alert.AlertType.INFORMATION, resultMessage.getResultString());
                    break;

            }
        } else if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_INSERT_ACCOUNT_INPUT_ISSUE:
                case FAIL_CODE_DELETE_ACCOUNT_ISSUE:
                case FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultMessage.getResultString());
                    break;
            }
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        log.info("button pressed: " + actionEvent.getSource());
        if (actionEvent.getSource() == jfxBReset) {
            updateUi(null);
        } else if (actionEvent.getSource() == jfxBInsert) {
            populateObject();
            new InsertAccountActionPerformed(this, account).actionPerform();
        } else if (actionEvent.getSource() == jfxBDelete) {
            new DeleteAccountActionPerformed(this, account).actionPerform();
        } else if (actionEvent.getSource() == jfxBUpdate) {
            populateObject();
            new UpdateAccountActionPerformed(this, account).actionPerform();
        }
    }

    @Override
    protected void updateUi(Account account) {
        log.info("updateUi, account: " + account);
        if (account == null) {
            account = null;
            dateTime = null;
            hodManage = null;
            vehicleAssigned = null;
            currentReading = 0;
            input = 0;
            output = 0;
            owner = null;

            jfxDatePicker.setValue(null);
            jfxTimePicker.setValue(null);
            MyEventBus.post(new HodManageRefreshEvent());
            MyEventBus.post(new VehicleAssignedRefreshEvent());
            jfxTFCurrentReading.setText(null);
            jfxTFInput.setText(null);
            jfxTFOutput.setText(null);
            jfxTFOwner.setText(null);

            jfxDatePicker.requestFocus();

            jfxBInsert.setDisable(false);
            jfxBDelete.setDisable(true);
            jfxBUpdate.setDisable(true);

        } else {
            this.account = account;
            dateTime = account.getDateTime();
            hodManage = account.getHodManage();
            vehicleAssigned = account.getVehicleAssigned();
            currentReading = account.getCurrentReading();
            input = account.getInput();
            output = account.getOutput();
            owner = account.getOwner();

            jfxDatePicker.setValue(account.getDateTime().toLocalDate());
            jfxTimePicker.setValue(account.getDateTime().toLocalTime());
            MyEventBus.post(new HodManageSelectionEvent(hodManage));
            MyEventBus.post(new VehicleAssignedSelectionEvent(vehicleAssigned));
            jfxTFCurrentReading.setText(String.valueOf(account.getCurrentReading()));
            jfxTFInput.setText(String.valueOf(account.getInput()));
            jfxTFOutput.setText(String.valueOf(account.getOutput()));
            jfxTFOwner.setText(account.getOwner());

            jfxBInsert.setDisable(true);
            jfxBDelete.setDisable(false);
            jfxBUpdate.setDisable(false);
        }
    }

    @Override
    protected void populateObject() {
        log.info("populateObject");
        if (account == null) {
            account = new Account();
        }

        if (jfxDatePicker.getValue() != null && jfxTimePicker.getValue() != null) {
            account.setDateTime(LocalDateTime.of(jfxDatePicker.getValue(), jfxTimePicker.getValue()));
        }

        account.setHodManage(hodManage);
        account.setVehicleAssigned(vehicleAssigned);
        account.setCurrentReading(Utility.getDoubleParseValue(jfxTFCurrentReading.getText()));
        account.setInput(Utility.getDoubleParseValue(jfxTFInput.getText()));
        account.setOutput(Utility.getDoubleParseValue(jfxTFOutput.getText()));
        account.setOwner(jfxTFOwner.getText());
    }

    @Subscribe
    public void selectHodManage(HodManageSelectionEvent hodManageSelectionEvent) {
        log.info("selectHodManage, hodManageSelectionEvent: " + hodManageSelectionEvent);
        if (hodManageSelectionEvent != null) {
            hodManage = hodManageSelectionEvent.getHodManage();
        }
    }

    @Subscribe
    public void selectVehicleAssigned(VehicleAssignedSelectionEvent vehicleAssignedSelectionEvent) {
        log.info("selectVehicleAssigned, vehicleAssignedSelectionEvent: " + vehicleAssignedSelectionEvent);
        if (vehicleAssignedSelectionEvent != null) {
            vehicleAssigned = vehicleAssignedSelectionEvent.getVehicleAssigned();
        }
    }

    @Subscribe
    public void selectAccount(AccountSelectionEvent accountSelectionEvent) {
        log.info("selectAccount, accountSelectionEvent: " + accountSelectionEvent);
        if (accountSelectionEvent != null) {
            updateUi(accountSelectionEvent.getAccount());
        }
    }
}
