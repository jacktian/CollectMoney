package com.yzdsmart.Collectmoney.http.response;

/**
 * Created by YZD on 2016/9/3.
 */
public class GetCoinRequestResponse {
    private Integer GoldNum;//本次获得金币
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public GetCoinRequestResponse() {
    }

    public GetCoinRequestResponse(Integer goldNum, String actionStatus, Integer errorCode, String errorInfo) {
        GoldNum = goldNum;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public Integer getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(Integer goldNum) {
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

    @Override
    public String toString() {
        return "{" +
                "GoldNum:" + GoldNum +
                ", ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                '}';
    }
}
