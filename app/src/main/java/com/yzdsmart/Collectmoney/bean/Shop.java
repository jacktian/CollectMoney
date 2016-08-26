package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/8/26.
 */
public class Shop {
    private String Code;//商铺编码
    private String Name;//商铺名称
    private String Addr;//商铺地址
    private Integer ReleGold;//当日金币数
    private String Coor;//坐标

    public Shop() {
    }

    public Shop(String code, String name, String addr, Integer releGold, String coor) {
        Code = code;
        Name = name;
        Addr = addr;
        ReleGold = releGold;
        Coor = coor;
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

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public Integer getReleGold() {
        return ReleGold;
    }

    public void setReleGold(Integer releGold) {
        ReleGold = releGold;
    }

    public String getCoor() {
        return Coor;
    }

    public void setCoor(String coor) {
        Coor = coor;
    }

    @Override
    public String toString() {
        return "{" +
                "Code:'" + Code + '\'' +
                ", Name:'" + Name + '\'' +
                ", Addr:'" + Addr + '\'' +
                ", ReleGold:" + ReleGold +
                ", Coor:'" + Coor + '\'' +
                '}';
    }
}
