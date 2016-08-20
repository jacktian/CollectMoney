package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/8/20.
 * 钱圈好友信息
 */
public class Friendship {
    private String avaterUrl;
    private String userName;
    private Integer userLevel;
    private Integer diamondCounts;

    public Friendship() {
    }

    public Friendship(String avaterUrl, String userName, Integer userLevel, Integer diamondCounts) {
        this.avaterUrl = avaterUrl;
        this.userName = userName;
        this.userLevel = userLevel;
        this.diamondCounts = diamondCounts;
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

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getDiamondCounts() {
        return diamondCounts;
    }

    public void setDiamondCounts(Integer diamondCounts) {
        this.diamondCounts = diamondCounts;
    }

    @Override
    public String toString() {
        return "{" +
                "avaterUrl:'" + avaterUrl + '\'' +
                ", userName:'" + userName + '\'' +
                ", userLevel:" + userLevel +
                ", diamondCounts:" + diamondCounts +
                '}';
    }
}
