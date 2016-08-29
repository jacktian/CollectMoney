package com.yzdsmart.Collectmoney.http.response;

import com.yzdsmart.Collectmoney.bean.TecentAccount;

/**
 * Created by YZD on 2016/8/27.
 */
public class LoginRequestResponse {
    private TecentAccount TCInfo;
    private String CustCode;
    private String SubmitCode;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public LoginRequestResponse() {
    }

    public LoginRequestResponse(TecentAccount TCInfo, String custCode, String submitCode, String actionStatus, Integer errorCode, String errorInfo) {
        this.TCInfo = TCInfo;
        CustCode = custCode;
        SubmitCode = submitCode;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
    }

    public TecentAccount getTCInfo() {
        return TCInfo;
    }

    public void setTCInfo(TecentAccount TCInfo) {
        this.TCInfo = TCInfo;
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
                "TCInfo:" + TCInfo +
                ", CustCode:'" + CustCode + '\'' +
                ", SubmitCode:'" + SubmitCode + '\'' +
                ", ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                '}';
    }
}
