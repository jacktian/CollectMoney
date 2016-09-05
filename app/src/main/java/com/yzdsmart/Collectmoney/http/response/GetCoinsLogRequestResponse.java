package com.yzdsmart.Collectmoney.http.response;

import com.yzdsmart.Collectmoney.bean.GetCoinsLog;

import java.util.List;

/**
 * Created by YZD on 2016/9/5.
 */
public class GetCoinsLogRequestResponse {
    private List<GetCoinsLog> lists;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public GetCoinsLogRequestResponse() {
    }

    public GetCoinsLogRequestResponse(List<GetCoinsLog> lists, String actionStatus, Integer errorCode, String errorInfo) {
        this.lists = lists;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public List<GetCoinsLog> getLists() {
        return lists;
    }

    public void setLists(List<GetCoinsLog> lists) {
        this.lists = lists;
    }

    public String getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        ErrorInfo = errorInfo;
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

    @Override
    public String toString() {
        return "{" +
                "lists:" + lists +
                ", ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                '}';
    }
}
