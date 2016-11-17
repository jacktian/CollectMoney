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

    private int Id;
    private String BazaCode;
    private int Amount;
    private int Gold;
    private String OpType;
    private String PayStatus;
    private String ChargeId;
    private String CreateTime;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String BazaCode) {
        this.BazaCode = BazaCode;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int Amount) {
        this.Amount = Amount;
    }

    public int getGold() {
        return Gold;
    }

    public void setGold(int Gold) {
        this.Gold = Gold;
    }

    public String getOpType() {
        return OpType;
    }

    public void setOpType(String OpType) {
        this.OpType = OpType;
    }

    public String getPayStatus() {
        return PayStatus;
    }

    public void setPayStatus(String PayStatus) {
        this.PayStatus = PayStatus;
    }

    public String getChargeId() {
        return ChargeId;
    }

    public void setChargeId(String ChargeId) {
        this.ChargeId = ChargeId;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
