package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/9/5.
 */
public class GetCoinsLog {
    private String BazaCode;//获取金币的商铺编码（加密）
    private Integer GetGold;//获取金币数
    private String TimeStr;//时间间隔
    private String CreateTime;//获取金币的时间

    public GetCoinsLog() {
    }

    public GetCoinsLog(String bazaCode, Integer getGold, String timeStr, String createTime) {
        BazaCode = bazaCode;
        GetGold = getGold;
        TimeStr = timeStr;
        CreateTime = createTime;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public Integer getGetGold() {
        return GetGold;
    }

    public void setGetGold(Integer getGold) {
        GetGold = getGold;
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

    @Override
    public String toString() {
        return "{" +
                "BazaCode:'" + BazaCode + '\'' +
                ", GetGold:" + GetGold +
                ", TimeStr:'" + TimeStr + '\'' +
                ", CreateTime:'" + CreateTime + '\'' +
                '}';
    }
}
