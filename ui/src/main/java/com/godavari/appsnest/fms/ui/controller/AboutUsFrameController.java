package com.godavari.appsnest.fms.ui.controller;

import com.godavari.appsnest.fms.ui.controller.report.BaseReportController;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.AboutUsSelectionEvent;
import com.godavari.appsnest.fms.ui.utility.ResourceString;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutUsFrameController extends BaseFrameController {

    public static final String ABOUT_COMPANY_SELECTED = "ABOUT_COMPANY_SELECTED";
    public static final String ABOUT_APPLICATION_SELECTED = "ABOUT_APPLICATION_SELECTED";

    @FXML
    private ImageView ivIcon;

    @FXML
    private JFXTextArea taContent;

    @FXML
    private Label lBuildVersion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        MyEventBus.register(this);

        Package aPackage = AboutUsFrameController.class.getPackage();
        lBuildVersion.setText("Build Version: "+aPackage.getImplementationVersion());
    }

    @Override
    protected void updateUi(Object object) {

    }

    @Override
    protected void populateObject() {

    }

    @Override
    public void handle(Event event) {

    }

    @Subscribe
    public void aboutUsSelected(AboutUsSelectionEvent aboutUsSelectionEvent) {
        if (aboutUsSelectionEvent != null) {
            if (!StringUtils.isEmpty(aboutUsSelectionEvent.getAboutUsTypeSelected())) {
                switch (aboutUsSelectionEvent.getAboutUsTypeSelected()) {
                    case ABOUT_COMPANY_SELECTED:
                        ivIcon.setImage(new Image(getClass().getClassLoader().getResource("image/company_logo.png").toString()
                                , true));
                        taContent.setText(ResourceString.getString("about_apps_nest"));
                        break;
                    case ABOUT_APPLICATION_SELECTED:
                        ivIcon.setImage(new Image(getClass().getClassLoader().getResource("image/application_logo.png").toString()
                                , true));
                        taContent.setText(ResourceString.getString("about_application"));
                        break;
                }
            }
        }
    }
}
