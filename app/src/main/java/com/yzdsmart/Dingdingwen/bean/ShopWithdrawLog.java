package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/24.
 */

public class ShopWithdrawLog {
    private String BazaCode;//商铺编码（加密）
    private Integer Gold;//兑换金币数
    private String Cash;//提现金额（人民币）
    private String TimeStr;//时间间隔
    private String CreateTime;//操作时间
    private String PayStatus;//支付状态

    public ShopWithdrawLog() {
    }

    public ShopWithdrawLog(String bazaCode, Integer gold, String cash, String timeStr, String createTime, String payStatus) {
        BazaCode = bazaCode;
        Gold = gold;
        Cash = cash;
        TimeStr = timeStr;
        CreateTime = createTime;
        PayStatus = payStatus;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public Integer getGold() {
        return Gold;
    }

    public void setGold(Integer gold) {
        Gold = gold;
    }

    public String getCash() {
        return Cash;
    }

    public void setCash(String cash) {
        Cash = cash;
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

    public String getPayStatus() {
        return PayStatus;
    }

    public void setPayStatus(String payStatus) {
        PayStatus = payStatus;
    }

    @Override
    public String toString() {
        return "{" +
                "BazaCode:'" + BazaCode + '\'' +
                ", Gold:" + Gold +
                ", Cash:'" + Cash + '\'' +
                ", TimeStr:'" + TimeStr + '\'' +
                ", CreateTime:'" + CreateTime + '\'' +
                ", PayStatus:'" + PayStatus + '\'' +
                '}';
    }
}
