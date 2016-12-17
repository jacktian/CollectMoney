package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/5.
 */
public class BuyCoinsLog {
    private String BazaCode;//商铺编码（加密）
    private Float TotalGold;//购买的金币数
    private Float OverGold;//剩余未使用金币
    private String CreateTime;//创建日期

    public BuyCoinsLog() {
    }

    public BuyCoinsLog(String bazaCode, Float totalGold, Float overGold, String createTime) {
        BazaCode = bazaCode;
        TotalGold = totalGold;
        OverGold = overGold;
        CreateTime = createTime;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public Float getTotalGold() {
        return TotalGold;
    }

    public void setTotalGold(Float totalGold) {
        TotalGold = totalGold;
    }

    public Float getOverGold() {
        return OverGold;
    }

    public void setOverGold(Float overGold) {
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
