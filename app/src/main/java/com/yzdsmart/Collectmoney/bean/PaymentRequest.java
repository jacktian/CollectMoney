package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/10/24.
 */

public class PaymentRequest {
    String channel;
    int amount;

    public PaymentRequest() {
    }

    public PaymentRequest(String channel, int amount) {
        this.channel = channel;
        this.amount = amount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{" +
                "channel:'" + channel + '\'' +
                ", amount:" + amount +
                '}';
    }
}
