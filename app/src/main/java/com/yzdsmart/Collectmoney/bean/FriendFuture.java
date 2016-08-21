package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/8/21.
 * 新朋友
 */
public class FriendFuture {
    private String avaterUrl;
    private String userName;
    private Integer futureType;//0 未处理 1 已添加

    public FriendFuture() {
    }

    public FriendFuture(String avaterUrl, String userName, Integer futureType) {
        this.avaterUrl = avaterUrl;
        this.userName = userName;
        this.futureType = futureType;
    }

    public String getAvaterUrl() {
        return avaterUrl;
    }

    public void setAvaterUrl(String avaterUrl) {
        this.avaterUrl = avaterUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getFutureType() {
        return futureType;
    }

    public void setFutureType(Integer futureType) {
        this.futureType = futureType;
    }

    @Override
    public String toString() {
        return "{" +
                "avaterUrl:'" + avaterUrl + '\'' +
                ", userName:'" + userName + '\'' +
                ", futureType:" + futureType +
                '}';
    }
}
