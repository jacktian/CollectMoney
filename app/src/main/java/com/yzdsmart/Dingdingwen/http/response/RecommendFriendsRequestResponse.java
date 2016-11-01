package com.yzdsmart.Dingdingwen.http.response;

import com.yzdsmart.Dingdingwen.bean.Friendship;

import java.util.List;

/**
 * Created by YZD on 2016/9/21.
 */

public class RecommendFriendsRequestResponse {
    private List<Friendship> lists;
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public RecommendFriendsRequestResponse() {
    }

    public RecommendFriendsRequestResponse(List<Friendship> lists, String actionStatus, Integer errorCode, String errorInfo) {
        this.lists = lists;
        ActionStatus = actionStatus;
        ErrorCode = errorCode;
        ErrorInfo = errorInfo;
    }

    public List<Friendship> getLists() {
        return lists;
    }

    public void setLists(List<Friendship> lists) {
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
