package com.godavari.appsnest.fms.ui.controller.eventlistener;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

@Log4j
public class JFXTextFieldChangeListener implements ChangeListener<String> {

    public static final int INPUT_TYPE_STRING = 0;
    public static final int INPUT_TYPE_INTEGER = 1;
    public static final int INPUT_TYPE_DOUBLE = 2;

    private int inputType = INPUT_TYPE_STRING;

    private JFXTextField jfxTextField;

    private boolean negativeValuePossible = false;

    public JFXTextFieldChangeListener(JFXTextField jfxTextField, int inputType) {
        this.jfxTextField = jfxTextField;
        this.inputType = inputType;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        try {
            if (newValue.startsWith("-") && negativeValuePossible) {
                newValue += "0";
            }
            if (inputType == INPUT_TYPE_INTEGER) {
                Integer.parseInt(newValue);
            } else if (inputType == INPUT_TYPE_DOUBLE) {
                Double.parseDouble(newValue);
            } else if (inputType == INPUT_TYPE_STRING) {
            }
        } catch (Exception e) {
            if (StringUtils.isEmpty(newValue)) {
                jfxTextField.setText(newValue);
            } else {
                jfxTextField.setText(oldValue);
            }
        }
    }
}
