package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/12/18.
 */

public class PaymentParameter {

    /**
     * SubmitCode :
     * BazaCode : WTAzNDI5
     * CustCode : a9524621-6b74-42cc-b395-d7d521d5b4a4
     * UseGold : 1
     * Discount : 6.0
     * Total : 10.0
     * PayPara : {"Amount":3,"Channel":"wx","Currency":"cny","Subject":"叮叮蚊消费支付","Body":"消费支付","Client_IP":"127.0.0.1"}
     */

    private String SubmitCode;
    private String BazaCode;
    private String CustCode;
    private Double UseGold;
    private Double Discount;
    private Double Total;
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

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
    }

    public Double getUseGold() {
        return UseGold;
    }

    public void setUseGold(Double useGold) {
        UseGold = useGold;
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
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
         * Subject : 叮叮蚊消费支付
         * Body : 消费支付
         * Client_IP : 127.0.0.1
         */

        private Double Amount;
        private String Channel;
        private String Currency;
        private String Subject;
        private String Body;
        private String Client_IP;

        public Double getAmount() {
            return Amount;
        }

        public void setAmount(Double amount) {
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
