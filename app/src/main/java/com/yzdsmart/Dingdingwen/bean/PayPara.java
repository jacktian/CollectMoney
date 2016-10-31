package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/10/27.
 */

public class PayPara {
    private String Order_No;//单据编号（不必备）
    private Double Amount;//订单总金额（必须大于0），单位为对应币种的最小货币单位，人民币为分。如订单总金额为 1 元， amount 为 100，么么贷商户请查看申请的借贷金额范围。（必备）
    private String Channel;//支付使用的第三方支付渠道，详情参考 支付渠道属性值 。（必备）
    private String Currency;//3 位 ISO 货币代码，人民币为  cny 。（不必备）
    private String Subject;//商品标题，该参数最长为 32 个 Unicode 字符，银联全渠道（ upacp / upacp_wap ）限制在 32 个字节。（不必备）
    private String Body;//商品描述信息，该参数最长为 128 个 Unicode 字符， yeepay_wap 对于该参数长度限制为 100 个 Unicode 字符。（不必备）
    private String Client_IP;//发起支付请求客户端的 IP 地址，格式为 IPv4 整型，如 127.0.0.1。（不必备）

    public PayPara() {
    }

    public PayPara(String order_No, Double amount, String channel, String currency, String subject, String body, String client_IP) {
        Order_No = order_No;
        Amount = amount;
        Channel = channel;
        Currency = currency;
        Subject = subject;
        Body = body;
        Client_IP = client_IP;
    }

    public String getOrder_No() {
        return Order_No;
    }

    public void setOrder_No(String order_No) {
        Order_No = order_No;
    }

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

    @Override
    public String toString() {
        return "{" +
                "Order_No:'" + Order_No + '\'' +
                ", Amount:" + Amount +
                ", Channel:'" + Channel + '\'' +
                ", Currency:'" + Currency + '\'' +
                ", Subject:'" + Subject + '\'' +
                ", Body:'" + Body + '\'' +
                ", Client_IP:'" + Client_IP + '\'' +
                '}';
    }
}
