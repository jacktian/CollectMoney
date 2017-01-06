package com.yzdsmart.Dingdingwen.bean;

import java.math.BigDecimal;

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
    private BigDecimal GoldNum;
    private Integer GoldType;
    private PayParaBean PayPara;

    public String getSubmitCode() {
        return SubmitCode;
    }

    public void setSubmitCode(String submitCode) {
        SubmitCode = submitCode;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public BigDecimal getGoldNum() {
        return GoldNum;
    }

    public void setGoldNum(BigDecimal goldNum) {
        GoldNum = goldNum;
    }

    public Integer getGoldType() {
        return GoldType;
    }

    public void setGoldType(Integer goldType) {
        GoldType = goldType;
    }

    public PayParaBean getPayPara() {
        return PayPara;
    }

    public void setPayPara(PayParaBean payPara) {
        PayPara = payPara;
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

        private BigDecimal Amount;
        private String Channel;
        private String Currency;
        private String Subject;
        private String Body;
        private String Client_IP;

        public BigDecimal getAmount() {
            return Amount;
        }

        public void setAmount(BigDecimal amount) {
            Amount = amount;
        }

        public String getChannel() {
            return Channel;
        }

        public void setChannel(String channel) {
            Channel = channel;
        }

        public String getCurrency() {
            return Currency;
        }

        public void setCurrency(String currency) {
            Currency = currency;
        }

        public String getSubject() {
            return Subject;
        }

        public void setSubject(String subject) {
            Subject = subject;
        }

        public String getBody() {
            return Body;
        }

        public void setBody(String body) {
            Body = body;
        }

        public String getClient_IP() {
            return Client_IP;
        }

        public void setClient_IP(String client_IP) {
            Client_IP = client_IP;
        }
    }
}
