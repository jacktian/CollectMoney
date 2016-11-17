package com.yzdsmart.Dingdingwen.http.response;

import com.yzdsmart.Dingdingwen.bean.ShopPayLog;

import java.util.List;

/**
 * Created by YZD on 2016/11/17.
 */

public class ShopPayLogRequestResponse {
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private List<ShopPayLog> lists;
    private Integer lastsequence;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递

    public ShopPayLogRequestResponse() {
    }

    public ShopPayLogRequestResponse(String actionStatus, Integer errorCode, String errorInfo, List<ShopPayLog> lists) {
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

    public List<ShopPayLog> getLists() {
        return lists;
    }

    public void setLists(List<ShopPayLog> lists) {
        this.lists = lists;
    }

    public Integer getLastsequence() {
        return lastsequence;
    }

    public void setLastsequence(Integer lastsequence) {
        this.lastsequence = lastsequence;
    }
}
