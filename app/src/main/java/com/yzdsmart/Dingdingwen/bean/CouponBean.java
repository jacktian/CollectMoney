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
    private int GoldType; //金币类型，默认一站地金币：0
    private String GoldName;//金币名称
    private String LogoLink; //金币logo url
    private int GoldNum;//兑换所需金币数
    private String Show;//兑换商品说明

    public void setExchangeId(String ExchangeId) {
        this.ExchangeId = ExchangeId;
    }

    public void setGoldType(int GoldType) {
        this.GoldType = GoldType;
    }

    public void setGoldName(String GoldName) {
        this.GoldName = GoldName;
    }

    public void setLogoLink(String LogoLink) {
        this.LogoLink = LogoLink;
    }

    public void setGoldNum(int GoldNum) {
        this.GoldNum = GoldNum;
    }

    public void setShow(String Show) {
        this.Show = Show;
    }

    public String getExchangeId() {
        return ExchangeId;
    }

    public int getGoldType() {
        return GoldType;
    }

    public String getGoldName() {
        return GoldName;
    }

    public String getLogoLink() {
        return LogoLink;
    }

    public int getGoldNum() {
        return GoldNum;
    }

    public String getShow() {
        return Show;
    }
}
