package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/10/9.
 */

public class ScannedLog {
    private String CustCode;//用户内码
    private String CNickName;//用户昵称
    private String ImageUrl;//头像路径
    private String CSex;//用户性别
    private String CAge;//用户年龄
    private Float Gold;//获取金币
    private String TimeStr;//时间间隔
    private String CreateTime;//扫码日期

    public ScannedLog() {
    }

    public ScannedLog(String custCode, String CNickName, String imageUrl, String CSex, String CAge, Float gold, String timeStr, String createTime) {
        CustCode = custCode;
        this.CNickName = CNickName;
        ImageUrl = imageUrl;
        this.CSex = CSex;
        this.CAge = CAge;
        Gold = gold;
        TimeStr = timeStr;
        CreateTime = createTime;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
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

    public String getCSex() {
        return CSex;
    }

    public void setCSex(String CSex) {
        this.CSex = CSex;
    }

    public String getCAge() {
        return CAge;
    }

    public void setCAge(String CAge) {
        this.CAge = CAge;
    }

    public Float getGold() {
        return Gold;
    }

    public void setGold(Float gold) {
        Gold = gold;
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
                "CustCode:'" + CustCode + '\'' +
                ", CNickName:'" + CNickName + '\'' +
                ", ImageUrl:'" + ImageUrl + '\'' +
                ", CSex:'" + CSex + '\'' +
                ", CAge:'" + CAge + '\'' +
                ", Gold:" + Gold +
                ", TimeStr:'" + TimeStr + '\'' +
                ", CreateTime:'" + CreateTime + '\'' +
                '}';
    }
}
