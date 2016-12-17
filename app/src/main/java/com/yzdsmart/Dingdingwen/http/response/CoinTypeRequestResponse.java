package com.yzdsmart.Dingdingwen.http.response;

import com.yzdsmart.Dingdingwen.bean.CoinType;

import java.util.List;

/**
 * Created by YZD on 2016/12/16.
 */

public class CoinTypeRequestResponse {

    /**
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     * Lists : [{"GoldType":0,"GoldName":"普通金币","LogoLink":""},{"GoldType":1,"GoldName":"金币1","LogoLink":""}]
     */

    private String ActionStatus;
    private int ErrorCode;
    private String ErrorInfo;
    private List<CoinType> Lists;

    public CoinTypeRequestResponse() {
    }

    public CoinTypeRequestResponse(String actionStatus, int errorCode, String errorInfo, List<CoinType> lists) {
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
        Lists = lists;
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

    public List<CoinType> getLists() {
        return Lists;
    }

    public void setLists(List<CoinType> lists) {
        Lists = lists;
    }
}
