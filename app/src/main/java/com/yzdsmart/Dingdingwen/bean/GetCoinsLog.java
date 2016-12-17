package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/5.
 */
public class GetCoinsLog {
    private String BazaCode;//获取金币的商铺编码（加密）
    private String BazaName;//获取商铺名称
    private String LogoImageUrl;//Logo图片链接
    private Float GetGold;//获取金币数
    private String TimeStr;//时间间隔
    private String CreateTime;//获取金币的时间

    public GetCoinsLog() {
    }

    public GetCoinsLog(String bazaCode, String bazaName, String logoImageUrl, Float getGold, String timeStr, String createTime) {
        BazaCode = bazaCode;
        BazaName = bazaName;
        LogoImageUrl = logoImageUrl;
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

    public String getBazaName() {
        return BazaName;
    }

    public void setBazaName(String bazaName) {
        BazaName = bazaName;
    }

    public String getLogoImageUrl() {
        return LogoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        LogoImageUrl = logoImageUrl;
    }

    public Float getGetGold() {
        return GetGold;
    }

    public void setGetGold(Float getGold) {
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
                "BazaName:'" + BazaName + '\'' +
                "LogoImageUrl:'" + LogoImageUrl + '\'' +
                ", GetGold:" + GetGold +
                ", TimeStr:'" + TimeStr + '\'' +
                ", CreateTime:'" + CreateTime + '\'' +
                '}';
    }
}
