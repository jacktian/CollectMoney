package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/24.
 */

public class ShopWithdrawLog {

    /**
     * BazaCode : WTAzNDI5
     * Gold : 1
     * Cash : 0.94
     * PayInfo : {"BankCode":"ICBC","BankCardNum":"6217231105000722122","CustName":"王海"}
     * TimeStr : 12小时前
     * PayStatus : 异常
     * CreateTime : 2016-09-23 20:37:50
     */

    private String BazaCode;
    private float Gold;
    private double Cash;
    private PayInfoBean PayInfo;
    private String TimeStr;
    private String PayStatus;
    private String CreateTime;

    public void setBazaCode(String BazaCode) {
        this.BazaCode = BazaCode;
    }

    public void setGold(int Gold) {
        this.Gold = Gold;
    }

    public void setCash(double Cash) {
        this.Cash = Cash;
    }

    public void setPayInfo(PayInfoBean PayInfo) {
        this.PayInfo = PayInfo;
    }

    public void setTimeStr(String TimeStr) {
        this.TimeStr = TimeStr;
    }

    public void setPayStatus(String PayStatus) {
        this.PayStatus = PayStatus;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public float getGold() {
        return Gold;
    }

    public double getCash() {
        return Cash;
    }

    public PayInfoBean getPayInfo() {
        return PayInfo;
    }

    public String getTimeStr() {
        return TimeStr;
    }

    public String getPayStatus() {
        return PayStatus;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public static class PayInfoBean {
        /**
         * BankCode : ICBC
         * BankCardNum : 6217231105000722122
         * CustName : 王海
         */

        private String BankCode;
        private String BankCardNum;
        private String CustName;

        public void setBankCode(String BankCode) {
            this.BankCode = BankCode;
        }

        public void setBankCardNum(String BankCardNum) {
            this.BankCardNum = BankCardNum;
        }

        public void setCustName(String CustName) {
            this.CustName = CustName;
        }

        public String getBankCode() {
            return BankCode;
        }

        public String getBankCardNum() {
            return BankCardNum;
        }

        public String getCustName() {
            return CustName;
        }
    }
}
