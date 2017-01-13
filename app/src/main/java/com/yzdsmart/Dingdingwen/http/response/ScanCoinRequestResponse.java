package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/9/3.
 */
public class ScanCoinRequestResponse {
    private Double GoldNum;//本次获得金币
    private String GoldLogoUrl;//金币图片
    private String Info;//本点签到成功
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public ScanCoinRequestResponse() {
    }

    public Double getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(Double goldNum) {
        GoldNum = goldNum;
    }

    public String getGoldLogoUrl() {
        return GoldLogoUrl;
    }

    public void setGoldLogoUrl(String goldLogoUrl) {
        GoldLogoUrl = goldLogoUrl;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
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
}
