package com.yzdsmart.Collectmoney.http.response;

/**
 * Created by jacks on 2016/8/28.
 */
public class UploadFileRequestResponse {
    private String RelaImageUrl;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public UploadFileRequestResponse() {
    }

    public UploadFileRequestResponse(String relaImageUrl, String actionStatus, Integer errorCode, String errorInfo) {
        RelaImageUrl = relaImageUrl;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public String getRelaImageUrl() {
        return RelaImageUrl;
    }

    public void setRelaImageUrl(String relaImageUrl) {
        RelaImageUrl = relaImageUrl;
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
                "RelaImageUrl:'" + RelaImageUrl + '\'' +
                ", ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                '}';
    }
}
