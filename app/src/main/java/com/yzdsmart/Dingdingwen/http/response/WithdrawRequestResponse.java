package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/9/3.
 */
public class WithdrawRequestResponse {
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private String Cash;//提现金额（人民币）

    public WithdrawRequestResponse() {
    }

    public WithdrawRequestResponse(String actionStatus, Integer errorCode, String errorInfo, String cash) {
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
        Cash = cash;
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

    public String getCash() {
        return Cash;
    }

    public void setCash(String cash) {
        Cash = cash;
    }

    @Override
    public String toString() {
        return "{" +
                "ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                ", Cash:'" + Cash + '\'' +
                '}';
    }
}
