package com.godavari.appsnest.fms.core.listener;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;

public abstract class BaseActionPerformedListener {
    protected IOnActionPerformed onActionPerformed;

    public BaseActionPerformedListener(IOnActionPerformed onActionPerformed)
    {
        this.onActionPerformed = onActionPerformed;
    }

    public abstract ResultMessage preActionPerformCheck();

    public abstract void actionPerform();
}
