package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/19.
 */
public class FocusedShop {
    private String BazaCode;//商铺编码（加密）
    private String Addr;//商铺地址
    private String Coor;//商铺坐标
    private String Name;//商铺名称
    private String Pers;//联系人
    private String Tel;//联系电话
    private Integer TodayGlodNum;//当日金币数
    private String LogoImageUrl;//Logo图片链接

    public FocusedShop() {
    }

    public FocusedShop(String bazaCode, String addr, String coor, String name, String pers, String tel, Integer todayGlodNum, String logoImageUrl) {
        BazaCode = bazaCode;
        Addr = addr;
        Coor = coor;
        Name = name;
        Pers = pers;
        Tel = tel;
        TodayGlodNum = todayGlodNum;
        LogoImageUrl = logoImageUrl;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getCoor() {
        return Coor;
    }

    public void setCoor(String coor) {
        Coor = coor;
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

    public Integer getTodayGlodNum() {
        return TodayGlodNum;
    }

    public void setTodayGlodNum(Integer todayGlodNum) {
        TodayGlodNum = todayGlodNum;
    }

    public String getLogoImageUrl() {
        return LogoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        LogoImageUrl = logoImageUrl;
    }

    @Override
    public String toString() {
        return "{" +
                "BazaCode:'" + BazaCode + '\'' +
                ", Addr:'" + Addr + '\'' +
                ", Coor:'" + Coor + '\'' +
                ", Name:'" + Name + '\'' +
                ", Pers:'" + Pers + '\'' +
                ", Tel:'" + Tel + '\'' +
                ", TodayGlodNum:" + TodayGlodNum +
                ", LogoImageUrl:" + LogoImageUrl +
                '}';
    }
}
