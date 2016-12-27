package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/12/27.
 */

public class ShopTaskLeftCoinsRequestResponse {

    /**
     * GoldNum : 9979
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     */

    private Double GoldNum;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public Double getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(Double goldNum) {
        GoldNum = goldNum;
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
}
