package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/12/14.
 */

public class PersonalWithdrawParameter {

    /**
     * SubmitCode :
     * CustCode : 67dd44de-dfca-4bdc-aa5e-7c5f0e68c91a
     * GoldNum : 2
     * GoldType : 0
     * PayInfo : {"BankCode":"ICBC","BankCardNum":"6217231105000722122","CustName":"王海"}
     */

    private String SubmitCode;
    private String CustCode;
    private int GoldNum;
    private int GoldType;
    private PayInfoBean PayInfo;

    public void setSubmitCode(String SubmitCode) {
        this.SubmitCode = SubmitCode;
    }

    public void setCustCode(String CustCode) {
        this.CustCode = CustCode;
    }

    public void setGoldNum(int GoldNum) {
        this.GoldNum = GoldNum;
    }

    public void setGoldType(int GoldType) {
        this.GoldType = GoldType;
    }

    public void setPayInfo(PayInfoBean PayInfo) {
        this.PayInfo = PayInfo;
    }

    public String getSubmitCode() {
        return SubmitCode;
    }

    public String getCustCode() {
        return CustCode;
    }

    public int getGoldNum() {
        return GoldNum;
    }

    public int getGoldType() {
        return GoldType;
    }

    public PayInfoBean getPayInfo() {
        return PayInfo;
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
