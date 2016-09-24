package com.yzdsmart.Collectmoney.http.response;

import com.yzdsmart.Collectmoney.bean.BuyCoinsLog;
import com.yzdsmart.Collectmoney.bean.ShopWithdrawLog;

import java.util.List;

/**
 * Created by YZD on 2016/9/5.
 */
public class ShopWithdrawLogRequestResponse {
    private List<ShopWithdrawLog> lists;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public ShopWithdrawLogRequestResponse() {
    }

    public ShopWithdrawLogRequestResponse(List<ShopWithdrawLog> lists, String actionStatus, Integer errorCode, String errorInfo) {
        this.lists = lists;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public List<ShopWithdrawLog> getLists() {
        return lists;
    }

    public void setLists(List<ShopWithdrawLog> lists) {
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
