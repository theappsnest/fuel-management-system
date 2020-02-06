package com.godavari.appsnest.fms.ui.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Optional;

@Log4j
public class Utility {
    public static final String TEXT_CONNECTOR = ", ";

    public static Optional<ButtonType> showDialogBox(Alert.AlertType alertType, String contentMessage, ButtonType... buttons) {
        Alert alert = new Alert(alertType, contentMessage);
        if (buttons != null) {
            alert.getButtonTypes().addAll(buttons);
        }
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Utility.class.getClassLoader().getResource("stylesheet/dialog_box_stylesheet.css").toExternalForm());
        return alert.showAndWait();
    }

    public static int getIntegerParseValue(String value) {
        return Integer.parseInt(getAppropriateParsingValue(value));
    }

    private static String getAppropriateParsingValue(String value) {
        String newValue = "";
        if (StringUtils.isEmpty(value)) {
            newValue = "0";
        } else {
            newValue = value;
        }
        return newValue;
    }

    public static double getDoubleParseValue(String value) {
        return Double.parseDouble(getAppropriateParsingValue(value));
    }

    public static Parent getParent(String url) {
        try {
            return FXMLLoader.load(Utility.class.getClassLoader().getResource(url), ResourceString.getResourceBundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File saveFileChooser(Window window, String title, String initialFileName) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(title);
        fileChooser.setInitialFileName(initialFileName);
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("save report", "xlsx"));

        File fileSelected = fileChooser.showSaveDialog(window);

        if (fileSelected != null) {
            log.info("File selected for saving: path" + fileSelected.getAbsolutePath());
        } else {
            log.warn("File not selected for saving");
        }
        return fileSelected;
    }
}
