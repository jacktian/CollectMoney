package com.yzdsmart.Collectmoney.http.response;

import com.yzdsmart.Collectmoney.bean.ScannedLog;

import java.util.List;

/**
 * Created by YZD on 2016/10/9.
 */

public class ScannedLogRequestResponse {
    private List<ScannedLog> lists;
    private Integer lastsequence;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public ScannedLogRequestResponse() {
    }

    public ScannedLogRequestResponse(List<ScannedLog> lists, Integer lastsequence, String actionStatus, Integer errorCode, String errorInfo) {
        this.lists = lists;
        this.lastsequence = lastsequence;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public List<ScannedLog> getLists() {
        return lists;
    }

    public void setLists(List<ScannedLog> lists) {
        this.lists = lists;
    }

    public Integer getLastsequence() {
        return lastsequence;
    }

    public void setLastsequence(Integer lastsequence) {
        this.lastsequence = lastsequence;
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
                "lastsequence:" + lastsequence +
                ", ActionStatus:'" + ActionStatus + '\'' +
                ", ErrorCode:" + ErrorCode +
                ", ErrorInfo:'" + ErrorInfo + '\'' +
                '}';
    }
}
