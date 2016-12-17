package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/9/3.
 */
public class GetCoinRequestResponse {
    private Float GoldNum;//本次获得金币
    private String GoldLogoUrl;//金币图片
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public GetCoinRequestResponse() {
    }

    public GetCoinRequestResponse(Float goldNum, String goldLogoUrl, String actionStatus, Integer errorCode, String errorInfo) {
        GoldNum = goldNum;
        GoldLogoUrl = goldLogoUrl;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public Float getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(Float goldNum) {
        GoldNum = goldNum;
    }

    public String getGoldLogoUrl() {
        return GoldLogoUrl;
    }

    public void setGoldLogoUrl(String goldLogoUrl) {
        GoldLogoUrl = goldLogoUrl;
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
