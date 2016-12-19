package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/8/28.
 */
public class ShopListRequestResponse {
    private String Code;//商铺编码
    private String Name;//商铺名称
    private String Addr;//商铺地址
    private Double ReleGold;//当日金币数
    private String Coor;//坐标

    public ShopListRequestResponse() {
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

    public Double getReleGold() {
        return ReleGold;
    }

    public void setReleGold(Double releGold) {
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
