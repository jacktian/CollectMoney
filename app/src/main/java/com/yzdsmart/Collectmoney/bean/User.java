package com.yzdsmart.Collectmoney.bean;

/**
 * 用户等级和星级
 * Created by YZD on 2016/8/27.
 */
public class User {
    private String C_Code;//用户内码
    private String C_UserCode;//用户名
    private String CName;//姓名
    private String TCAccount;//云通讯帐号
    private String NickName;//昵称
    private String ImageUrl;//头像
    private Integer Gra;//等级
    private Integer Sta;//星级

    public User() {
    }

    public User(String c_Code, String c_UserCode, String CName, String TCAccount, String nickName, String imageUrl, Integer gra, Integer sta) {
        C_Code = c_Code;
        C_UserCode = c_UserCode;
        this.CName = CName;
        this.TCAccount = TCAccount;
        NickName = nickName;
        ImageUrl = imageUrl;
        Gra = gra;
        Sta = sta;
    }

    public String getC_Code() {
        return C_Code;
    }

    public void setC_Code(String c_Code) {
        C_Code = c_Code;
    }

    public String getC_UserCode() {
        return C_UserCode;
    }

    public void setC_UserCode(String c_UserCode) {
        C_UserCode = c_UserCode;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getTCAccount() {
        return TCAccount;
    }

    public void setTCAccount(String TCAccount) {
        this.TCAccount = TCAccount;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Integer getGra() {
        return Gra;
    }

    public void setGra(Integer gra) {
        Gra = gra;
    }

    public Integer getSta() {
        return Sta;
    }

    public void setSta(Integer sta) {
        Sta = sta;
    }

    @Override
    public String toString() {
        return "{" +
                "C_Code:'" + C_Code + '\'' +
                ", C_UserCode:'" + C_UserCode + '\'' +
                ", CName:'" + CName + '\'' +
                ", TCAccount:'" + TCAccount + '\'' +
                ", NickName:'" + NickName + '\'' +
                ", ImageUrl:'" + ImageUrl + '\'' +
                ", Gra:" + Gra +
                ", Sta:" + Sta +
                '}';
    }
}
