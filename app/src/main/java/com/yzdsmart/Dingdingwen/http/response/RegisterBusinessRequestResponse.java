package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/9/4.
 */
public class RegisterBusinessRequestResponse {
    private String BazaCode;//注册商铺加密编号
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public RegisterBusinessRequestResponse() {
    }

    public RegisterBusinessRequestResponse(String bazaCode, String actionStatus, Integer errorCode, String errorInfo) {
        BazaCode = bazaCode;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
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
                "BazaCode:'" + BazaCode + '\'' +
                ", ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                '}';
    }
}
