package com.yzdsmart.Collectmoney.http.response;

/**
 * Created by YZD on 2016/8/29.
 */
public class CustInfoRequestResponse {
    private String C_Code;//用户内码
    private String C_UserCode;//用户名
    private String CName;//用户名
    private String NickName;//用户昵称
    private String ImageUrl;//头像URL
    private String Area;//所在地区  例如：江苏 常州
    private Integer OperNum;//操作次数：找钱，扫码（现在还没有获取）
    private Integer GoldNum;//金币数（现在还没有获取）
    private Integer FriendNum;//朋友数量（现在还没有获取）

    public CustInfoRequestResponse() {
    }

    public CustInfoRequestResponse(String c_Code, String c_UserCode, String CName, String nickName, String imageUrl, String area, Integer operNum, Integer goldNum, Integer friendNum) {
        C_Code = c_Code;
        C_UserCode = c_UserCode;
        this.CName = CName;
        NickName = nickName;
        ImageUrl = imageUrl;
        Area = area;
        OperNum = operNum;
        GoldNum = goldNum;
        FriendNum = friendNum;
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

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public Integer getOperNum() {
        return OperNum;
    }

    public void setOperNum(Integer operNum) {
        OperNum = operNum;
    }

    public Integer getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(Integer goldNum) {
        GoldNum = goldNum;
    }

    public Integer getFriendNum() {
        return FriendNum;
    }

    public void setFriendNum(Integer friendNum) {
        FriendNum = friendNum;
    }

    @Override
    public String toString() {
        return "{" +
                "C_Code:'" + C_Code + '\'' +
                ", C_UserCode:'" + C_UserCode + '\'' +
                ", CName:'" + CName + '\'' +
                ", NickName:'" + NickName + '\'' +
                ", ImageUrl:'" + ImageUrl + '\'' +
                ", Area:'" + Area + '\'' +
                ", OperNum:" + OperNum +
                ", GoldNum:" + GoldNum +
                ", FriendNum:" + FriendNum +
                '}';
    }
}
