package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/24.
 */

public class PersonalWithdrawLog {

    /**
     * CustCode : f47faba3-5bb8-4bd4-b844-206c80503704
     * Gold : 2
     * Cash : 1.60
     * PayInfo : {"BankCode":"ICBC","BankCardNum":"6217231105000722122","CustName":"王海"}
     * TimeStr : 11小时前
     * PayStatus : 异常
     * CreateTime : 2016-09-23 21:20:21
     */

    private String CustCode;
    private float Gold;
    private String Cash;
    private PayInfoBean PayInfo;
    private String TimeStr;
    private String PayStatus;
    private String CreateTime;

    public void setCustCode(String CustCode) {
        this.CustCode = CustCode;
    }

    public void setGold(int Gold) {
        this.Gold = Gold;
    }

    public void setCash(String Cash) {
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

    public String getCustCode() {
        return CustCode;
    }

    public float getGold() {
        return Gold;
    }

    public String getCash() {
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
