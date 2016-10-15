package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/8/20.
 * 钱圈好友信息
 */
public class Friendship {
    private String C_Code;//用户内码
    private String C_UserCode;//用户名
    private String CName;//姓名
    private String CSex;//性别：男或者女（必填）或者空字符串
    private String CBirthday;//生日  格式为：yyyy-MM-dd 或者空字符串
    private String TCAccount;//云通讯帐号
    private String NickName;//昵称
    private String ImageUrl;//头像
    private String GroupName;//所在群组
    private String Remark;//好友备注
    private Integer Gra;//等级
    private Integer Sta;//星级

    public Friendship() {
    }

    public Friendship(String c_Code, String c_UserCode, String CName, String CSex, String CBirthday, String TCAccount, String nickName, String imageUrl, String groupName, String remark, Integer gra, Integer sta) {
        C_Code = c_Code;
        C_UserCode = c_UserCode;
        this.CName = CName;
        this.CSex = CSex;
        this.CBirthday = CBirthday;
        this.TCAccount = TCAccount;
        NickName = nickName;
        ImageUrl = imageUrl;
        GroupName = groupName;
        Remark = remark;
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

    public String getCSex() {
        return CSex;
    }

    public void setCSex(String CSex) {
        this.CSex = CSex;
    }

    public String getCBirthday() {
        return CBirthday;
    }

    public void setCBirthday(String CBirthday) {
        this.CBirthday = CBirthday;
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

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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
                ", CSex:'" + CSex + '\'' +
                ", CBirthday:'" + CBirthday + '\'' +
                ", TCAccount:'" + TCAccount + '\'' +
                ", NickName:'" + NickName + '\'' +
                ", ImageUrl:'" + ImageUrl + '\'' +
                ", GroupName:'" + GroupName + '\'' +
                ", Remark:'" + Remark + '\'' +
                ", Gra:" + Gra +
                ", Sta:" + Sta +
                '}';
    }
}
