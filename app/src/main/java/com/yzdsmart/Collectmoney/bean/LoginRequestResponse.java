package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/8/27.
 */
public class LoginRequestResponse {
    private TecentAccount tecentAccount;
    private String SubmitCode;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public LoginRequestResponse() {
    }

    public LoginRequestResponse(TecentAccount tecentAccount, String submitCode, String actionStatus, Integer errorCode, String errorInfo) {
        this.tecentAccount = tecentAccount;
        SubmitCode = submitCode;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public TecentAccount getTecentAccount() {
        return tecentAccount;
    }

    public void setTecentAccount(TecentAccount tecentAccount) {
        this.tecentAccount = tecentAccount;
    }

    public String getSubmitCode() {
        return SubmitCode;
    }

    public void setSubmitCode(String submitCode) {
        SubmitCode = submitCode;
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
                "tecentAccount:" + tecentAccount +
                ", SubmitCode:'" + SubmitCode + '\'' +
                ", ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                '}';
    }
}
