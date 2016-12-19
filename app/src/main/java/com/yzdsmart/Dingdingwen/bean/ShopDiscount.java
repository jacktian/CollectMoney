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
    private Double DiscReta;
    private Double FullPrice;
    private Double DiscPrice;

    public ShopDiscount() {
    }

    public Integer getDisType() {
        return DisType;
    }

    public void setDisType(Integer disType) {
        DisType = disType;
    }

    public Double getDiscReta() {
        return DiscReta;
    }

    public void setDiscReta(Double discReta) {
        DiscReta = discReta;
    }

    public Double getFullPrice() {
        return FullPrice;
    }

    public void setFullPrice(Double fullPrice) {
        FullPrice = fullPrice;
    }

    public Double getDiscPrice() {
        return DiscPrice;
    }

    public void setDiscPrice(Double discPrice) {
        DiscPrice = discPrice;
    }
}
