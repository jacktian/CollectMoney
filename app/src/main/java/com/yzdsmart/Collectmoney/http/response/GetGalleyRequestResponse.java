package com.yzdsmart.Collectmoney.http.response;

import com.yzdsmart.Collectmoney.bean.GalleyInfo;

import java.util.List;

/**
 * Created by YZD on 2016/9/21.
 */

public class GetGalleyRequestResponse {
    private List<GalleyInfo> lists;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public GetGalleyRequestResponse() {
    }

    public GetGalleyRequestResponse(List<GalleyInfo> lists, String actionStatus, Integer errorCode, String errorInfo) {
        this.lists = lists;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public List<GalleyInfo> getLists() {
        return lists;
    }

    public void setLists(List<GalleyInfo> lists) {
        this.lists = lists;
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
                "lists:" + lists +
                ", ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                '}';
    }
}
