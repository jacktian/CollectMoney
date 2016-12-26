package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/12/26.
 */

public class PaymentLog {

    /**
     * BazaCode : R1cwMTc2
     * BazaName : 康桥小馄炖
     * GoldNum : 2.0
     * PayAmount : 4.0
     * Amount : 8.0
     * TimeStr : 1天前
     * CreateTime : 2016-09-03 01:05:01
     */
    private String CustCode;
    private String BazaCode;
    private String BazaName;
    private String CNickName;
    private String ImageUrl;
    private String LogoImageUrl;
    private Double GoldNum;
    private Double PayAmount;
    private Double Amount;
    private String TimeStr;
    private String CreateTime;

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public String getBazaName() {
        return BazaName;
    }

    public void setBazaName(String bazaName) {
        BazaName = bazaName;
    }

    public String getCNickName() {
        return CNickName;
    }

    public void setCNickName(String CNickName) {
        this.CNickName = CNickName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLogoImageUrl() {
        return LogoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        LogoImageUrl = logoImageUrl;
    }

    public Double getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(Double goldNum) {
        GoldNum = goldNum;
    }

    public Double getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(Double payAmount) {
        PayAmount = payAmount;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public String getTimeStr() {
        return TimeStr;
    }

    public void setTimeStr(String timeStr) {
        TimeStr = timeStr;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
