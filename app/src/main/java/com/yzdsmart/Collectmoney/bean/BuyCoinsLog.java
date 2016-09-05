package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/9/5.
 */
public class BuyCoinsLog {
    private String BazaCode;//商铺编码（加密）
    private Integer TotalGold;//购买的金币数
    private Integer OverGold;//剩余未使用金币
    private String CreateTime;//创建日期

    public BuyCoinsLog() {
    }

    public BuyCoinsLog(String bazaCode, Integer totalGold, Integer overGold, String createTime) {
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

    public Integer getTotalGold() {
        return TotalGold;
    }

    public void setTotalGold(Integer totalGold) {
        TotalGold = totalGold;
    }

    public Integer getOverGold() {
        return OverGold;
    }

    public void setOverGold(Integer overGold) {
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
