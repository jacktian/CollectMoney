package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/8/22.
 */
public class ShopScanner {
    private String CustCode;//用户内码
    private String CustPwdName;//用户隐藏的部分姓名
    private String ImageUrl;//用户的头像Url
    private String CustSex;//用户性别
    private Integer GoldNum;//获取的金币
    private String TimeStr;//时间间隔
    private String TCAccount;//用户云通讯用户

    public ShopScanner() {
    }

    public ShopScanner(String custCode, String custPwdName, String imageUrl, String custSex, Integer goldNum, String timeStr, String TCAccount) {
        CustCode = custCode;
        CustPwdName = custPwdName;
        ImageUrl = imageUrl;
        CustSex = custSex;
        GoldNum = goldNum;
        TimeStr = timeStr;
        this.TCAccount = TCAccount;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
    }

    public String getCustPwdName() {
        return CustPwdName;
    }

    public void setCustPwdName(String custPwdName) {
        CustPwdName = custPwdName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getCustSex() {
        return CustSex;
    }

    public void setCustSex(String custSex) {
        CustSex = custSex;
    }

    public Integer getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(Integer goldNum) {
        GoldNum = goldNum;
    }

    public String getTimeStr() {
        return TimeStr;
    }

    public void setTimeStr(String timeStr) {
        TimeStr = timeStr;
    }

    public String getTCAccount() {
        return TCAccount;
    }

    public void setTCAccount(String TCAccount) {
        this.TCAccount = TCAccount;
    }

    @Override
    public String toString() {
        return "{" +
                "CustCode:'" + CustCode + '\'' +
                ", CustPwdName:'" + CustPwdName + '\'' +
                ", ImageUrl:'" + ImageUrl + '\'' +
                ", CustSex:'" + CustSex + '\'' +
                ", GoldNum:" + GoldNum +
                ", TimeStr:'" + TimeStr + '\'' +
                ", TCAccount:'" + TCAccount + '\'' +
                '}';
    }
}
