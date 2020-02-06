package com.godavari.appsnest.fms.ui.eventbus.event;

import com.godavari.appsnest.fms.ui.eventbus.Postable;
import lombok.Getter;

@Getter
public class AboutUsSelectionEvent implements Postable {
    private String aboutUsTypeSelected;

    public AboutUsSelectionEvent(String aboutUsTypeSelected) {
        this.aboutUsTypeSelected = aboutUsTypeSelected;
    }
}
