package com.yzdsmart.Dingdingwen.http.response;

import com.yzdsmart.Dingdingwen.bean.PaymentLog;

import java.util.List;

/**
 * Created by YZD on 2016/12/26.
 */

public class PaymentLogRequestResponse {

    /**
     * lists : [{"BazaCode":"R1cwMTc2","BazaName":"康桥小馄炖","GoldNum":2,"PayAmount":4,"Amount":8,"TimeStr":"1天前","CreateTime":"2016-09-03 01:05:01"},{"BazaCode":"R1cwMTc2","BazaName":"康桥小馄炖","GoldNum":1,"PayAmount":10,"Amount":15,"TimeStr":"1天前","CreateTime":"2016-09-02 09:27:20"}]
     * lastsequence : 11
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     */

    private Integer lastsequence;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private List<PaymentLog> lists;

    public Integer getLastsequence() {
        return lastsequence;
    }

    public void setLastsequence(Integer lastsequence) {
        this.lastsequence = lastsequence;
    }

    public String getActionStatus() {
        return ActionStatus;
    }

    public void setActionStatus(String actionStatus) {
        ActionStatus = actionStatus;
    }

    public Integer getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(Integer errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        ErrorInfo = errorInfo;
    }

    public List<PaymentLog> getLists() {
        return lists;
    }

    public void setLists(List<PaymentLog> lists) {
        this.lists = lists;
    }
}
