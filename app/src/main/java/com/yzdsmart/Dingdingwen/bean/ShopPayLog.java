package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/11/17.
 */

public class ShopPayLog {

    /**
     * Id : 26
     * BazaCode : WTAzNDI5
     * Amount : 1
     * Gold : 1
     * OpType : 商铺充值
     * PayStatus : 未支付
     * ChargeId : ch_9iXD4C9abXrD8qjDWT1e1irD
     * CreateTime : 2016-11-15 16:25:42
     */

    private Integer Id;
    private String BazaCode;
    private Double Amount;
    private Double Gold;
    private String OpType;
    private String PayStatus;
    private String ChargeId;
    private String CreateTime;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public Double getGold() {
        return Gold;
    }

    public void setGold(Double gold) {
        Gold = gold;
    }

    public String getOpType() {
        return OpType;
    }

    public void setOpType(String opType) {
        OpType = opType;
    }

    public String getPayStatus() {
        return PayStatus;
    }

    public void setPayStatus(String payStatus) {
        PayStatus = payStatus;
    }

    public String getChargeId() {
        return ChargeId;
    }

    public void setChargeId(String chargeId) {
        ChargeId = chargeId;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
