package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/12/19.
 */

public class CouponBean {
    /**
     * ExchangeId : 1TAzNDI5
     * GoldType : 0
     * GoldName : 普通金币
     * LogoLink :
     * GoldNum : 5
     * Show : 兑换一张优惠券
     */

    private String ExchangeId;//兑换内码
    private Integer GoldType; //金币类型，默认一站地金币：0
    private String GoldName;//金币名称
    private String LogoLink; //金币logo url
    private Double GoldNum;//兑换所需金币数
    private String Show;//兑换商品说明

    public String getExchangeId() {
        return ExchangeId;
    }

    public void setExchangeId(String exchangeId) {
        ExchangeId = exchangeId;
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

    public Double getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(Double goldNum) {
        GoldNum = goldNum;
    }

    public String getShow() {
        return Show;
    }

    public void setShow(String show) {
        Show = show;
    }
}
