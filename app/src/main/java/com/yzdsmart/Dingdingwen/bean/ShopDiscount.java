package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/12/18.
 */

public class ShopDiscount {

    /**
     * 商铺打折信息
     * DisType : 23
     * DiscReta : 0.85
     * FullPrice : 0.0
     * DiscPrice : 0.0
     */

    private Integer DisType;
    private Float DiscReta;
    private Float FullPrice;
    private Float DiscPrice;

    public ShopDiscount() {
    }

    public ShopDiscount(Integer disType, Float discReta, Float fullPrice, Float discPrice) {
        DisType = disType;
        DiscReta = discReta;
        FullPrice = fullPrice;
        DiscPrice = discPrice;
    }

    public Integer getDisType() {
        return DisType;
    }

    public void setDisType(Integer disType) {
        DisType = disType;
    }

    public Float getDiscReta() {
        return DiscReta;
    }

    public void setDiscReta(Float discReta) {
        DiscReta = discReta;
    }

    public Float getFullPrice() {
        return FullPrice;
    }

    public void setFullPrice(Float fullPrice) {
        FullPrice = fullPrice;
    }

    public Float getDiscPrice() {
        return DiscPrice;
    }

    public void setDiscPrice(Float discPrice) {
        DiscPrice = discPrice;
    }
}
