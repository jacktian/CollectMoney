package com.yzdsmart.Dingdingwen.http.response;

import com.yzdsmart.Dingdingwen.bean.FocusedShop;

import java.util.List;

/**
 * Created by YZD on 2016/9/19.
 */
public class FocusedShopRequestResponse {
    private List<FocusedShop> lists;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public FocusedShopRequestResponse() {
    }

    public FocusedShopRequestResponse(List<FocusedShop> lists, String actionStatus, Integer errorCode, String errorInfo) {
        this.lists = lists;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public List<FocusedShop> getLists() {
        return lists;
    }

    public void setLists(List<FocusedShop> lists) {
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
