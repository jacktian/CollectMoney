package com.yzdsmart.Collectmoney.http.response;

/**
 * 网络请求返回信息
 * Created by YZD on 2016/8/26.
 */
public class RequestResponse {
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private String ErrorDisplay;

    public RequestResponse() {
    }

    public RequestResponse(String actionStatus, Integer errorCode, String errorInfo, String errorDisplay) {
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
        ErrorDisplay = errorDisplay;
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

    public String getErrorDisplay() {
        return ErrorDisplay;
    }

    public void setErrorDisplay(String errorDisplay) {
        ErrorDisplay = errorDisplay;
    }

    @Override
    public String toString() {
        return "{" +
                "ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                ", ErrorDisplay:'" + ErrorDisplay + '\'' +
                '}';
    }
}
