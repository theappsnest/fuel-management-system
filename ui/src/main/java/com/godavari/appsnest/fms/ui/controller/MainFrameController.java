package com.godavari.appsnest.fms.ui.controller;

import com.godavari.appsnest.fms.ui.controller.report.BaseReportController;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.Subscriber;
import com.godavari.appsnest.fms.ui.eventbus.event.AboutUsSelectionEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.NavigationDrawerSelectionEvent;
import com.godavari.appsnest.fms.ui.utility.ResourceString;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class MainFrameController<T> extends BaseReportController<T> {

    @FXML
    private JFXHamburger jfxHamburger;

    @FXML
    private JFXDrawer jfxDrawer;

    @FXML
    private MenuItem miAboutApplication;

    @FXML
    private MenuItem miAboutCompany;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane paneFrame;

    private HamburgerBasicCloseTransition burgerTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            log.info("initialize");
            MyEventBus.register(this);

            VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("layout/list/navigation_drawer_layout.fxml"), ResourceString.getResourceBundle());
            jfxDrawer.setSidePane(vBox);
            jfxDrawer.setOnDrawerOpening(event ->
            {
                AnchorPane.setRightAnchor(jfxDrawer, 750.0);
                AnchorPane.setLeftAnchor(jfxDrawer, 0.0);
                AnchorPane.setTopAnchor(jfxDrawer, 0.0);
                AnchorPane.setBottomAnchor(jfxDrawer, 0.0);
            });

            jfxDrawer.setOnDrawerClosed(event ->
            {
                AnchorPane.clearConstraints(jfxDrawer);
                AnchorPane.setLeftAnchor(jfxDrawer, -255.0);
                AnchorPane.setTopAnchor(jfxDrawer, 0.0);
                AnchorPane.setBottomAnchor(jfxDrawer, 0.0);
            });

            burgerTask = new HamburgerBasicCloseTransition(jfxHamburger);
            burgerTask.setRate(-1);
            jfxHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();

                if (jfxDrawer.isOpened()) {
                    jfxDrawer.close();
                } else {
                    jfxDrawer.open();
                }
            });

            jfxHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });

            miAboutApplication.setOnAction(this);
            miAboutApplication.setVisible(false);
            miAboutCompany.setOnAction(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void populateObject() {

    }

    @Override
    public void handle(ActionEvent actionEvent) {
        log.info("handle, actionEvent: " + actionEvent);
        if (actionEvent.getSource() == miAboutCompany) {
            showAboutDialog(AboutUsFrameController.ABOUT_COMPANY_SELECTED, ResourceString.getString("company_name"));
        } else if (actionEvent.getSource() == miAboutApplication) {
            showAboutDialog(AboutUsFrameController.ABOUT_APPLICATION_SELECTED,ResourceString.getString("application_name"));
        }
    }

    @Subscribe
    public void navigationDrawerSelection(NavigationDrawerSelectionEvent navigationDrawerSelectionEvent) {
        if (navigationDrawerSelectionEvent != null) {
            String url = navigationDrawerSelectionEvent.getResourceUrl();
            jfxDrawer.close();

            if (!anchorPane.getChildren().isEmpty()) {
                anchorPane.getChildren().remove(0);
            }

            anchorPane.getChildren().add(0, Utility.getParent(url));
            AnchorPane.setBottomAnchor(anchorPane.getChildren().get(0), 0d);
            AnchorPane.setTopAnchor(anchorPane.getChildren().get(0), 0d);
            AnchorPane.setLeftAnchor(anchorPane.getChildren().get(0), 0d);
            AnchorPane.setRightAnchor(anchorPane.getChildren().get(0), 0d);
        }
    }

    private void showAboutDialog(String aboutUsTypeSelected, String title) {
        log.info("showAboutDialog");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layout/about_us_frame_layout.fxml"), ResourceString.getResourceBundle());
            Parent parent = loader.load();
            MyEventBus.post(new AboutUsSelectionEvent(aboutUsTypeSelected));

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle(title);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setContent(parent);
            dialogPane.getStylesheets().add(Utility.class.getClassLoader().getResource("stylesheet/about_us_stylesheet.css").toExternalForm());
            alert.getButtonTypes().add(ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            log.error("exception in loading about dialog box", e);
        }
    }
}
