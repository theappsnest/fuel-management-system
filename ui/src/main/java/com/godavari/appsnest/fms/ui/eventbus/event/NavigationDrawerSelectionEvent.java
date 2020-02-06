package com.godavari.appsnest.fms.ui.eventbus.event;

import com.godavari.appsnest.fms.ui.eventbus.Postable;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class NavigationDrawerSelectionEvent implements Postable {

    private String resourceUrl;

    public NavigationDrawerSelectionEvent(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
