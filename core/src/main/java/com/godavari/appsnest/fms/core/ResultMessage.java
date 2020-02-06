package com.godavari.appsnest.fms.core;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class ResultMessage {

    private int resultType;
    private int resultCode;
    private String resultString;

    public ResultMessage(int resultType, int resultCode, String resultString) {
        this.resultType = resultType;
        this.resultCode = resultCode;
        this.resultString = resultString;
    }
}
