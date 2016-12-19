package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/12/16.
 */

public class CoinType {
    private Integer GoldType;
    private Double GCount;
    private String GoldName;
    private String LogoLink;

    public CoinType() {
    }

    public CoinType(Integer goldType, Double GCount, String goldName, String logoLink) {
        GoldType = goldType;
        this.GCount = GCount;
        GoldName = goldName;
        LogoLink = logoLink;
    }

    public Integer getGoldType() {
        return GoldType;
    }

    public void setGoldType(Integer goldType) {
        GoldType = goldType;
    }

    public Double getGCount() {
        return GCount;
    }

    public void setGCount(Double GCount) {
        this.GCount = GCount;
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
