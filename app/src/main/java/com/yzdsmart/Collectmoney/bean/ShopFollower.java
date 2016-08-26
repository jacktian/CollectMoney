package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/8/22.
 */
public class ShopFollower {
    private String avaterUrl;
    private String userName;
    private Integer coins;
    private String time;

    public ShopFollower() {
    }

    public ShopFollower(String avaterUrl, String userName, Integer coins, String time) {
        this.avaterUrl = avaterUrl;
        this.userName = userName;
        this.coins = coins;
        this.time = time;
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

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{" +
                "avaterUrl:'" + avaterUrl + '\'' +
                ", userName:'" + userName + '\'' +
                ", coins:" + coins +
                ", time:'" + time + '\'' +
                '}';
    }
}
