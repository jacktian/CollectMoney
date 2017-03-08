package com.yzdsmart.Dingdingwen.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YZD on 2017/3/8.
 */

public class MarketShop implements Parcelable {

    /**
     * ImageUrl :
     * Code : WTY2V0VJ
     * Name : 美勒贝尔
     * Addr : 环球港4F
     * ReleGold : 1.25
     * Coor : 119.9791628000,31.8401970700
     */

    private String ImageUrl;
    private String Code;
    private String Name;
    private String Addr;
    private Double ReleGold;
    private String Coor;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String Addr) {
        this.Addr = Addr;
    }

    public Double getReleGold() {
        return ReleGold;
    }

    public void setReleGold(Double ReleGold) {
        this.ReleGold = ReleGold;
    }

    public String getCoor() {
        return Coor;
    }

    public void setCoor(String Coor) {
        this.Coor = Coor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ImageUrl);
        dest.writeString(this.Code);
        dest.writeString(this.Name);
        dest.writeString(this.Addr);
        dest.writeValue(this.ReleGold);
        dest.writeString(this.Coor);
    }

    public MarketShop() {
    }

    protected MarketShop(Parcel in) {
        this.ImageUrl = in.readString();
        this.Code = in.readString();
        this.Name = in.readString();
        this.Addr = in.readString();
        this.ReleGold = (Double) in.readValue(Double.class.getClassLoader());
        this.Coor = in.readString();
    }

    public static final Parcelable.Creator<MarketShop> CREATOR = new Parcelable.Creator<MarketShop>() {
        @Override
        public MarketShop createFromParcel(Parcel source) {
            return new MarketShop(source);
        }

        @Override
        public MarketShop[] newArray(int size) {
            return new MarketShop[size];
        }
    };
}
