package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/10/27.
 */

public class BuyCoinParameter {

    /**
     * SubmitCode :
     * BazaCode : WTQzMDM0
     * GoldNum : 3
     * GoldType : 0
     * PayPara : {"Amount":3,"Channel":"wx","Currency":"cny","Subject":"叮叮蚊支付","Body":"充值金币","Client_IP":"127.0.0.1"}
     */

    private String SubmitCode;
    private String BazaCode;
    private int GoldNum;
    private int GoldType;
    private PayParaBean PayPara;

    public void setSubmitCode(String SubmitCode) {
        this.SubmitCode = SubmitCode;
    }

    public void setBazaCode(String BazaCode) {
        this.BazaCode = BazaCode;
    }

    public void setGoldNum(int GoldNum) {
        this.GoldNum = GoldNum;
    }

    public void setGoldType(int GoldType) {
        this.GoldType = GoldType;
    }

    public void setPayPara(PayParaBean PayPara) {
        this.PayPara = PayPara;
    }

    public String getSubmitCode() {
        return SubmitCode;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public int getGoldNum() {
        return GoldNum;
    }

    public int getGoldType() {
        return GoldType;
    }

    public PayParaBean getPayPara() {
        return PayPara;
    }

    public static class PayParaBean {
        /**
         * Amount : 3.0
         * Channel : wx
         * Currency : cny
         * Subject : 叮叮蚊支付
         * Body : 充值金币
         * Client_IP : 127.0.0.1
         */

        private double Amount;
        private String Channel;
        private String Currency;
        private String Subject;
        private String Body;
        private String Client_IP;

        public void setAmount(double Amount) {
            this.Amount = Amount;
        }

        public void setChannel(String Channel) {
            this.Channel = Channel;
        }

        public void setCurrency(String Currency) {
            this.Currency = Currency;
        }

        public void setSubject(String Subject) {
            this.Subject = Subject;
        }

        public void setBody(String Body) {
            this.Body = Body;
        }

        public void setClient_IP(String Client_IP) {
            this.Client_IP = Client_IP;
        }

        public double getAmount() {
            return Amount;
        }

        public String getChannel() {
            return Channel;
        }

        public String getCurrency() {
            return Currency;
        }

        public String getSubject() {
            return Subject;
        }

        public String getBody() {
            return Body;
        }

        public String getClient_IP() {
            return Client_IP;
        }
    }
}
