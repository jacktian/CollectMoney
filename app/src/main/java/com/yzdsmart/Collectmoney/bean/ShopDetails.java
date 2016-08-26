package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/8/26.
 */
public class ShopDetails {
    private String Code;//商铺编码
    private String Name;//商铺名称
    private String Pers;//联系人
    private String Tel;//联系电话
    private String Addr;//地址
    private String Remark;//备注
    private String Coor; //坐标
    private Boolean IsAtte;//当前用户是否关注
    private Integer AtteNum;//关注人次
    private Integer TodayGlodNum;//今日金币数量
    private Integer VisiNum;//访问总人次

    public ShopDetails() {
    }

    public ShopDetails(String code, String name, String pers, String tel, String addr, String remark, String coor, Boolean isAtte, Integer atteNum, Integer todayGlodNum, Integer visiNum) {
        Code = code;
        Name = name;
        Pers = pers;
        Tel = tel;
        Addr = addr;
        Remark = remark;
        Coor = coor;
        IsAtte = isAtte;
        AtteNum = atteNum;
        TodayGlodNum = todayGlodNum;
        VisiNum = visiNum;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPers() {
        return Pers;
    }

    public void setPers(String pers) {
        Pers = pers;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCoor() {
        return Coor;
    }

    public void setCoor(String coor) {
        Coor = coor;
    }

    public Boolean getAtte() {
        return IsAtte;
    }

    public void setAtte(Boolean atte) {
        IsAtte = atte;
    }

    public Integer getAtteNum() {
        return AtteNum;
    }

    public void setAtteNum(Integer atteNum) {
        AtteNum = atteNum;
    }

    public Integer getTodayGlodNum() {
        return TodayGlodNum;
    }

    public void setTodayGlodNum(Integer todayGlodNum) {
        TodayGlodNum = todayGlodNum;
    }

    public Integer getVisiNum() {
        return VisiNum;
    }

    public void setVisiNum(Integer visiNum) {
        VisiNum = visiNum;
    }

    @Override
    public String toString() {
        return "{" +
                "Code:'" + Code + '\'' +
                ", Name:'" + Name + '\'' +
                ", Pers:'" + Pers + '\'' +
                ", Tel:'" + Tel + '\'' +
                ", Addr:'" + Addr + '\'' +
                ", Remark:'" + Remark + '\'' +
                ", Coor:'" + Coor + '\'' +
                ", IsAtte:" + IsAtte +
                ", AtteNum:" + AtteNum +
                ", TodayGlodNum:" + TodayGlodNum +
                ", VisiNum:" + VisiNum +
                '}';
    }
}
