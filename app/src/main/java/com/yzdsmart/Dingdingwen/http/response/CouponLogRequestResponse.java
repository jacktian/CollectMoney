package com.yzdsmart.Dingdingwen.http.response;

import com.yzdsmart.Dingdingwen.bean.CouponLog;

import java.util.List;

/**
 * Created by YZD on 2016/12/26.
 */

public class CouponLogRequestResponse {

    /**
     * lists : [{"BazaCode":"R1cwMTc2","BazaName":"康桥小馄炖","GoldNum":2,"GoldName":"特殊金币","Show":"价值十元的玩具","TimeStr":"1天前","CreateTime":"2016-09-03 01:05:01"},{"BazaCode":"R1cwMTc2","BazaName":"康桥小馄炖","GoldNum":5,"GoldName":"特殊金币","Show":"价值10元的代金券","TimeStr":"1天前","CreateTime":"2016-09-02 09:27:20"}]
     * lastsequence : 11
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     */

    private int lastsequence;
    private String ActionStatus;
    private int ErrorCode;
    private String ErrorInfo;
    private List<CouponLog> lists;

    public int getLastsequence() {
        return lastsequence;
    }

    public void setLastsequence(int lastsequence) {
        this.lastsequence = lastsequence;
    }

    public String getActionStatus() {
        return ActionStatus;
    }

    public void setActionStatus(String actionStatus) {
        ActionStatus = actionStatus;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        ErrorInfo = errorInfo;
    }

    public List<CouponLog> getLists() {
        return lists;
    }

    public void setLists(List<CouponLog> lists) {
        this.lists = lists;
    }
}
