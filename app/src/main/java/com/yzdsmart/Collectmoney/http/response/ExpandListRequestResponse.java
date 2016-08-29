package com.yzdsmart.Collectmoney.http.response;

/**
 * Created by jacks on 2016/8/28.
 */
public class ExpandListRequestResponse {
    private String Name;
    private String Addr;
    private String ImageUrl;
    private String Coor;

    public ExpandListRequestResponse() {
    }

    public ExpandListRequestResponse(String name, String addr, String imageUrl, String coor) {
        Name = name;
        Addr = addr;
        ImageUrl = imageUrl;
        Coor = coor;
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

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
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
                "Name:'" + Name + '\'' +
                ", Addr:'" + Addr + '\'' +
                ", ImageUrl:'" + ImageUrl + '\'' +
                ", Coor:'" + Coor + '\'' +
                '}';
    }
}
