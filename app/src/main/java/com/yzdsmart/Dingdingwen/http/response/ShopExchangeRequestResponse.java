package com.yzdsmart.Dingdingwen.http.response;

import com.yzdsmart.Dingdingwen.bean.CouponBean;

import java.util.List;

/**
 * Created by YZD on 2016/12/19.
 */

public class ShopExchangeRequestResponse {

    /**
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     * Lists : [{"ExchangeId":"1TAzNDI5","GoldType":0,"GoldName":"普通金币","LogoLink":"","GoldNum":5,"Show":"兑换一张优惠券"},{"ExchangeId":"1TAzNDI2","GoldType":0,"GoldName":"金币1","LogoLink":"","GoldNum":5,"Show":"兑换一个小礼品"}]
     */

    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private List<CouponBean> lists;

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

    public List<CouponBean> getLists() {
        return lists;
    }

    public void setLists(List<CouponBean> lists) {
        this.lists = lists;
    }
}
