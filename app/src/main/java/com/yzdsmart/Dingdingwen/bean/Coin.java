package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/12/16.
 */

public class Coin {
    private Integer GoldType;
    private String GoldName;
    private String LogoLink;

    public Coin() {
    }

    public Coin(Integer goldType, String goldName, String logoLink) {
        GoldType = goldType;
        GoldName = goldName;
        LogoLink = logoLink;
    }

    public Integer getGoldType() {
        return GoldType;
    }

    public void setGoldType(Integer goldType) {
        GoldType = goldType;
    }

    public String getGoldName() {
        return GoldName;
    }

    public void setGoldName(String goldName) {
        GoldName = goldName;
    }

    public String getLogoLink() {
        return LogoLink;
    }

    public void setLogoLink(String logoLink) {
        LogoLink = logoLink;
    }
}
