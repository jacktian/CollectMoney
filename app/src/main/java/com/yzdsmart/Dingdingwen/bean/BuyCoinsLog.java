package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/5.
 */
public class BuyCoinsLog {
    private String BazaCode;//商铺编码（加密）
    private Double TotalGold;//购买的金币数
    private Double OverGold;//剩余未使用金币
    private String CreateTime;//创建日期

    public BuyCoinsLog() {
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public Double getTotalGold() {
        return TotalGold;
    }

    public void setTotalGold(Double totalGold) {
        TotalGold = totalGold;
    }

    public Double getOverGold() {
        return OverGold;
    }

    public void setOverGold(Double overGold) {
        OverGold = overGold;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    @Override
    public String toString() {
        return "{" +
                "BazaCode:'" + BazaCode + '\'' +
                ", TotalGold:" + TotalGold +
                ", OverGold:" + OverGold +
                ", CreateTime:'" + CreateTime + '\'' +
                '}';
    }
}
