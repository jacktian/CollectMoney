package com.yzdsmart.Dingdingwen.http.response;

import com.yzdsmart.Dingdingwen.bean.CoinType;

import java.util.List;

/**
 * Created by YZD on 2016/12/18.
 */

public class BackgroundBagRequestResponse {
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private List<CoinType> lists;

    public BackgroundBagRequestResponse() {
    }

    public BackgroundBagRequestResponse(String actionStatus, Integer errorCode, String errorInfo, List<CoinType> lists) {
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
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

    public List<CoinType> getLists() {
        return lists;
    }

    public void setLists(List<CoinType> lists) {
        this.lists = lists;
    }
}
