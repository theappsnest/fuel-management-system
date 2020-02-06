package com.godavari.appsnest.fms.ui.eventbus;

import com.google.common.eventbus.EventBus;

public class MyEventBus {
    private static MyEventBus myEventBus;
    private static EventBus eventBus;

    static {
        myEventBus = new MyEventBus();
        eventBus = new EventBus();
    }

    public static void register(Subscriber subscribe)
    {
        eventBus.register(subscribe);
    }

    public static void post(Postable event) {
        eventBus.post(event);
    }
}
