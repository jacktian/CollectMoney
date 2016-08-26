package com.yzdsmart.Collectmoney.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YZD on 2016/8/26.
 */
public class ShopParcelable implements Parcelable {
    private Shop shop;

    public ShopParcelable() {
    }

    public ShopParcelable(Shop shop) {
        this.shop = shop;
    }

    protected ShopParcelable(Parcel in) {
        shop = new Shop();
        shop.setCode(in.readString());
        shop.setName(in.readString());
        shop.setAddr(in.readString());
        shop.setReleGold(in.readInt());
        shop.setCoor(in.readString());
    }

    public static final Creator<ShopParcelable> CREATOR = new Creator<ShopParcelable>() {
        @Override
        public ShopParcelable createFromParcel(Parcel in) {
            return new ShopParcelable(in);
        }

        @Override
        public ShopParcelable[] newArray(int size) {
            return new ShopParcelable[size];
        }
    };

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shop.getCode());
        parcel.writeString(shop.getName());
        parcel.writeString(shop.getAddr());
        parcel.writeInt(shop.getReleGold());
        parcel.writeString(shop.getCoor());
    }
}
