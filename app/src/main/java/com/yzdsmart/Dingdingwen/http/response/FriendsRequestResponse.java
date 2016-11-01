package com.yzdsmart.Dingdingwen.http.response;

import com.yzdsmart.Dingdingwen.bean.Friendship;

import java.util.List;

/**
 * Created by YZD on 2016/8/27.
 */
public class FriendsRequestResponse {
    private String C_Code;//用户内码
    private String C_UserCode;//用户名
    private String NeedUpdateAll;// 是否需要全量更新："GetAll_Type_YES"表示需要全量更新，"GetAll_Type_NO"表示不需要全量更新。
    private Long TimeStampNow;// 本次拉取的时间戳，客户端需要保存该时间，下次请求时通过TimeStamp字段返回给后台。
    private Integer StartIndex;// 拉取的起始位置。
    private Integer CurrentStandardSequence;// 本次拉取标配关系链的Sequence，客户端需要保存该Sequence，下次请求时通过LastStandardSequence字段返回给后台。
    private Integer FriendNum;// 好友总数量
    private List<Friendship> lists;//好友信息列表

    public FriendsRequestResponse() {
    }

    public FriendsRequestResponse(String c_Code, String c_UserCode, String needUpdateAll, Long timeStampNow, Integer startIndex, Integer currentStandardSequence, Integer friendNum, List<Friendship> lists) {
        C_Code = c_Code;
        C_UserCode = c_UserCode;
        NeedUpdateAll = needUpdateAll;
        TimeStampNow = timeStampNow;
        StartIndex = startIndex;
        CurrentStandardSequence = currentStandardSequence;
        FriendNum = friendNum;
        this.lists = lists;
    }

    public String getC_Code() {
        return C_Code;
    }

    public void setC_Code(String c_Code) {
        C_Code = c_Code;
    }

    public String getC_UserCode() {
        return C_UserCode;
    }

    public void setC_UserCode(String c_UserCode) {
        C_UserCode = c_UserCode;
    }

    public String getNeedUpdateAll() {
        return NeedUpdateAll;
    }

    public void setNeedUpdateAll(String needUpdateAll) {
        NeedUpdateAll = needUpdateAll;
    }

    public Long getTimeStampNow() {
        return TimeStampNow;
    }

    public void setTimeStampNow(Long timeStampNow) {
        TimeStampNow = timeStampNow;
    }

    public Integer getStartIndex() {
        return StartIndex;
    }

    public void setStartIndex(Integer startIndex) {
        StartIndex = startIndex;
    }

    public Integer getCurrentStandardSequence() {
        return CurrentStandardSequence;
    }

    public void setCurrentStandardSequence(Integer currentStandardSequence) {
        CurrentStandardSequence = currentStandardSequence;
    }

    public Integer getFriendNum() {
        return FriendNum;
    }

    public void setFriendNum(Integer friendNum) {
        FriendNum = friendNum;
    }

    public List<Friendship> getFriends() {
        return lists;
    }

    public void setFriends(List<Friendship> friends) {
        this.lists = friends;
    }

    @Override
    public String toString() {
        return "{" +
                "C_Code:'" + C_Code + '\'' +
                ", C_UserCode:'" + C_UserCode + '\'' +
                ", NeedUpdateAll:'" + NeedUpdateAll + '\'' +
                ", TimeStampNow:" + TimeStampNow +
                ", StartIndex:" + StartIndex +
                ", CurrentStandardSequence:" + CurrentStandardSequence +
                ", FriendNum:" + FriendNum +
                ", lists:" + lists +
                '}';
    }
}
