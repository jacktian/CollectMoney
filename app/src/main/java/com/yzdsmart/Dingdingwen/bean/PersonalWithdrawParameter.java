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
    private Double GoldNum;
    private Integer GoldType;
    private PayInfoBean PayInfo;

    public String getSubmitCode() {
        return SubmitCode;
    }

    public void setSubmitCode(String submitCode) {
        SubmitCode = submitCode;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
    }

    public Double getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(Double goldNum) {
        GoldNum = goldNum;
    }

    public Integer getGoldType() {
        return GoldType;
    }

    public void setGoldType(Integer goldType) {
        GoldType = goldType;
    }

    public PayInfoBean getPayInfo() {
        return PayInfo;
    }

    public void setPayInfo(PayInfoBean payInfo) {
        PayInfo = payInfo;
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
