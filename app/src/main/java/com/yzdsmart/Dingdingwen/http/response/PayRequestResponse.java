package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/10/27.
 */

public class PayRequestResponse {

    /**
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     * Charge : {"id":"ch_58iDK0u5iDyPOunfHS9ufTS4","object":"charge","created":1478848025,"livemode":false,"paid":false,"refunded":false,"app":"app_1Gqj58ynP0mHeX1q","channel":"wx","order_no":"20161111150632423C4J","client_ip":"127.0.0.1","amount":3,"amount_settle":3,"currency":"cny","subject":"叮叮蚊支付","body":"充值金币","extra":{},"time_paid":null,"time_expire":1478855225,"time_settle":null,"transaction_no":null,"refunds":{"object":"list","url":"/v1/charges/ch_58iDK0u5iDyPOunfHS9ufTS4/refunds","has_more":false,"data":[]},"amount_refunded":0,"failure_code":null,"failure_msg":null,"metadata":{},"credential":{"object":"credential","wx":{"appId":"wxp4ihq5y1sytowvjl","partnerId":"1238560201","prepayId":"1101000000161111unhwx1i1u5ggbdar","nonceStr":"b7a42472e7db8b89f870b69aa961a32d","timeStamp":1478848025,"packageValue":"Sign=WXPay","sign":"93985fe34315b751ce77222c540b73822a49f9de"}},"description":null}
     */

    private String ActionStatus;
    private int ErrorCode;
    private String ErrorInfo;
    private Object Charge;

    public String getActionStatus() {
        return ActionStatus;
    }

    public void setActionStatus(String ActionStatus) {
        this.ActionStatus = ActionStatus;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public String getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(String ErrorInfo) {
        this.ErrorInfo = ErrorInfo;
    }

    public Object getCharge() {
        return Charge;
    }

    public void setCharge(Object charge) {
        Charge = charge;
    }

}
