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
    private Double Gold;
    private Double Cash;
    private PayInfoBean PayInfo;
    private String TimeStr;
    private String PayStatus;
    private String CreateTime;

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public Double getGold() {
        return Gold;
    }

    public void setGold(Double gold) {
        Gold = gold;
    }

    public Double getCash() {
        return Cash;
    }

    public void setCash(Double cash) {
        Cash = cash;
    }

    public PayInfoBean getPayInfo() {
        return PayInfo;
    }

    public void setPayInfo(PayInfoBean payInfo) {
        PayInfo = payInfo;
    }

    public String getTimeStr() {
        return TimeStr;
    }

    public void setTimeStr(String timeStr) {
        TimeStr = timeStr;
    }

    public String getPayStatus() {
        return PayStatus;
    }

    public void setPayStatus(String payStatus) {
        PayStatus = payStatus;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
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
