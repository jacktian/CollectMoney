package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/9/20.
 */

public class ShopFocuser {
    private String CustCode;//会员编号
    private String CustSex;//会员性别
    private String ImageUrl;//会员头像
    private String CustPwdName;//会员姓名
    private String TCAccount;//腾讯云通讯账号

    public ShopFocuser() {
    }

    public ShopFocuser(String custCode, String custSex, String imageUrl, String custPwdName, String TCAccount) {
        CustCode = custCode;
        CustSex = custSex;
        ImageUrl = imageUrl;
        CustPwdName = custPwdName;
        this.TCAccount = TCAccount;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
    }

    public String getCustSex() {
        return CustSex;
    }

    public void setCustSex(String custSex) {
        CustSex = custSex;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getCustPwdName() {
        return CustPwdName;
    }

    public void setCustPwdName(String custPwdName) {
        CustPwdName = custPwdName;
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
                ", CustSex:'" + CustSex + '\'' +
                ", ImageUrl:'" + ImageUrl + '\'' +
                ", CustPwdName:'" + CustPwdName + '\'' +
                ", TCAccount:'" + TCAccount + '\'' +
                '}';
    }
}
