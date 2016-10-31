package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/10/27.
 */

public class PaymentRequest {

    /**
     * SubmitCode :
     * CustCode :
     * PayPara : {"Order_No":"1222221","Amount":3,"Channel":"wx","Currency":"cny","Subject":"test","Body":"121212","Client_IP":"127.0.0.1"}
     */

    private String SubmitCode;
    private String CustCode;
    /**
     * Order_No : 1222221
     * Amount : 3.0
     * Channel : wx
     * Currency : cny
     * Subject : test
     * Body : 121212
     * Client_IP : 127.0.0.1
     */

    private PayParaBean PayPara;

    public String getSubmitCode() {
        return SubmitCode;
    }

    public void setSubmitCode(String SubmitCode) {
        this.SubmitCode = SubmitCode;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String CustCode) {
        this.CustCode = CustCode;
    }

    public PayParaBean getPayPara() {
        return PayPara;
    }

    public void setPayPara(PayParaBean PayPara) {
        this.PayPara = PayPara;
    }

    public static class PayParaBean {
        private String Order_No;
        private double Amount;
        private String Channel;
        private String Currency;
        private String Subject;
        private String Body;
        private String Client_IP;

        public String getOrder_No() {
            return Order_No;
        }

        public void setOrder_No(String Order_No) {
            this.Order_No = Order_No;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double Amount) {
            this.Amount = Amount;
        }

        public String getChannel() {
            return Channel;
        }

        public void setChannel(String Channel) {
            this.Channel = Channel;
        }

        public String getCurrency() {
            return Currency;
        }

        public void setCurrency(String Currency) {
            this.Currency = Currency;
        }

        public String getSubject() {
            return Subject;
        }

        public void setSubject(String Subject) {
            this.Subject = Subject;
        }

        public String getBody() {
            return Body;
        }

        public void setBody(String Body) {
            this.Body = Body;
        }

        public String getClient_IP() {
            return Client_IP;
        }

        public void setClient_IP(String Client_IP) {
            this.Client_IP = Client_IP;
        }
    }
}
