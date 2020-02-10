package com.godavari.appsnest.fms.ui.controller.report;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.dao.utility.UtilityMethod;
import com.godavari.appsnest.fms.ui.controller.BaseFrameController;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;
import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.FAIL_CODE_EXCEPTION_THROWN;

@Log4j
public abstract class BaseReportController<T> extends BaseFrameController<T> {

    @FXML
    protected JFXDatePicker jfxDPFromDate;

    @FXML
    protected JFXDatePicker jfxDPToDate;

    @FXML
    protected JFXTextField tfJfxReportFileName;

    @FXML
    protected JFXTextField tfJfxReportAbsolutePathSelected;

    @FXML
    protected ImageView ivSelectReportFileDirectory;

    @FXML
    protected JFXButton jfxBReset;

    @FXML
    protected JFXButton jfxBGenerateReport;

    protected String reportType;

    protected String absoluteReportFilePathWithExtension;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        jfxDPFromDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) {
                if (newValue != null) {
                    setReportFileName();
                    jfxDPToDate.setValue(jfxDPFromDate.getValue().plusDays(1));
                }
            }
        });

        jfxDPToDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) {
                if (newValue != null) {
                    setReportFileName();
                }
            }
        });

        jfxDPToDate.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && jfxDPFromDate.getValue() != null && item.isBefore(jfxDPFromDate.getValue().plusDays(1))) {
                            setDisable(true);
                        }
                    }
                };
            }
        });

        jfxBReset.setOnAction(this);
        jfxBGenerateReport.setOnAction(this);

        ivSelectReportFileDirectory.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                log.info("select file directory folder icon click");
                saveFileChooser();
                event.consume();
            }
        });
    }

    private void saveFileChooser() {
        log.info("saveFileChooser");

        File pdfFileSelected = Utility.saveFileChooser(jfxBReset.getScene().getWindow(), "Save the Report xlsx file",
                tfJfxReportFileName.getText());

        if (pdfFileSelected != null) {
            String filePath = pdfFileSelected.getAbsolutePath();
            String fileName = pdfFileSelected.getName();
            absoluteReportFilePathWithExtension = filePath + UtilityMethod.XLSX_EXTENSION_STRING;
            tfJfxReportFileName.setText(fileName);
            tfJfxReportAbsolutePathSelected.setText(absoluteReportFilePathWithExtension);
            log.info("pdf folder selected is: " + absoluteReportFilePathWithExtension);
        }
    }

    @Override
    protected void updateUi(T object) {
        if (object == null) {
            jfxDPFromDate.setValue(null);
            jfxDPToDate.setValue(null);
            tfJfxReportFileName.setText(null);
            tfJfxReportAbsolutePathSelected.setText(null);

            absoluteReportFilePathWithExtension = "";

            jfxDPFromDate.requestFocus();
        }
    }

    protected void setReportFileName() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(reportType)) {
            stringBuilder.append(reportType + " ");
        }

        if (jfxDPFromDate.getValue() != null) {
            stringBuilder.append(jfxDPFromDate.getValue() + " ");
        }

        if (jfxDPToDate.getValue() != null) {
            stringBuilder.append(jfxDPToDate.getValue() + " ");
        }

        tfJfxReportFileName.setText(stringBuilder.toString());
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        super.onActionPerformedResult(resultMessage);
        if (resultMessage.getResultType() == RESULT_TYPE_FAIL) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_BASE_REPORT_INPUT_ISSUE:
                case FAIL_CODE_SQL_EXCEPTION_THROWN:
                case FAIL_CODE_EXCEPTION_THROWN:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultMessage.getResultString());
                    break;
            }
        }
    }
}
