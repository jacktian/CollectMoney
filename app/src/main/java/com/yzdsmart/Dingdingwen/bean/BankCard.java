package com.yzdsmart.Dingdingwen.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YZD on 2016/12/13.
 */

public class BankCard implements Parcelable {
    private String BankCode;//银行代码
    private String BankCardNum;//银行卡号（19位）
    private String BankName;//银行名称
    private String CustName;//持卡人姓名（长度20位）

    public BankCard() {
    }

    protected BankCard(Parcel in) {
        BankCode = in.readString();
        BankCardNum = in.readString();
        BankName = in.readString();
        CustName = in.readString();
    }

    public static final Creator<BankCard> CREATOR = new Creator<BankCard>() {
        @Override
        public BankCard createFromParcel(Parcel in) {
            return new BankCard(in);
        }

        @Override
        public BankCard[] newArray(int size) {
            return new BankCard[size];
        }
    };

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
    }

    public String getBankCardNum() {
        return BankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        BankCardNum = bankCardNum;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    @Override
    public String toString() {
        return "{" +
                "BankCode:'" + BankCode + '\'' +
                ", BankCardNum:'" + BankCardNum + '\'' +
                ", BankName:'" + BankName + '\'' +
                ", CustName:'" + CustName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(BankCode);
        parcel.writeString(BankCardNum);
        parcel.writeString(BankName);
        parcel.writeString(CustName);
    }
}
