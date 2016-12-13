package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/12/13.
 */

public class BankCard {
    private String BankCode;//银行代码
    private String BankCardNum;//银行卡号（19位）
    private String BankName;//银行名称
    private String CustName;//持卡人姓名（长度20位）

    public BankCard() {
    }

    public BankCard(String bankCode, String bankCardNum, String bankName, String custName) {
        BankCode = bankCode;
        BankCardNum = bankCardNum;
        BankName = bankName;
        CustName = custName;
    }

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
}
