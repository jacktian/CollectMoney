package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/10/27.
 */

public class BuyCoinsPayRequestResponse {

    /**
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     * Charge : {"id":"ch_58iDK0u5iDyPOunfHS9ufTS4","object":"charge","created":1478848025,"livemode":false,"paid":false,"refunded":false,"app":"app_1Gqj58ynP0mHeX1q","channel":"wx","order_no":"20161111150632423C4J","client_ip":"127.0.0.1","amount":3,"amount_settle":3,"currency":"cny","subject":"叮叮蚊支付","body":"充值金币","extra":{},"time_paid":null,"time_expire":1478855225,"time_settle":null,"transaction_no":null,"refunds":{"object":"list","url":"/v1/charges/ch_58iDK0u5iDyPOunfHS9ufTS4/refunds","has_more":false,"data":[]},"amount_refunded":0,"failure_code":null,"failure_msg":null,"metadata":{},"credential":{"object":"credential","wx":{"appId":"wxp4ihq5y1sytowvjl","partnerId":"1238560201","prepayId":"1101000000161111unhwx1i1u5ggbdar","nonceStr":"b7a42472e7db8b89f870b69aa961a32d","timeStamp":1478848025,"packageValue":"Sign=WXPay","sign":"93985fe34315b751ce77222c540b73822a49f9de"}},"description":null}
     */

    private String ActionStatus;
    private int ErrorCode;
    private String ErrorInfo;
    private ChargeBean Charge;

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

    public ChargeBean getCharge() {
        return Charge;
    }

    public void setCharge(ChargeBean Charge) {
        this.Charge = Charge;
    }

    public static class ChargeBean {
        /**
         * id : ch_58iDK0u5iDyPOunfHS9ufTS4
         * object : charge
         * created : 1478848025
         * livemode : false
         * paid : false
         * refunded : false
         * app : app_1Gqj58ynP0mHeX1q
         * channel : wx
         * order_no : 20161111150632423C4J
         * client_ip : 127.0.0.1
         * amount : 3
         * amount_settle : 3
         * currency : cny
         * subject : 叮叮蚊支付
         * body : 充值金币
         * extra : {}
         * time_paid : null
         * time_expire : 1478855225
         * time_settle : null
         * transaction_no : null
         * refunds : {"object":"list","url":"/v1/charges/ch_58iDK0u5iDyPOunfHS9ufTS4/refunds","has_more":false,"data":[]}
         * amount_refunded : 0
         * failure_code : null
         * failure_msg : null
         * metadata : {}
         * credential : {"object":"credential","wx":{"appId":"wxp4ihq5y1sytowvjl","partnerId":"1238560201","prepayId":"1101000000161111unhwx1i1u5ggbdar","nonceStr":"b7a42472e7db8b89f870b69aa961a32d","timeStamp":1478848025,"packageValue":"Sign=WXPay","sign":"93985fe34315b751ce77222c540b73822a49f9de"}}
         * description : null
         */

        private String id;
        private String object;
        private int created;
        private boolean livemode;
        private boolean paid;
        private boolean refunded;
        private String app;
        private String channel;
        private String order_no;
        private String client_ip;
        private int amount;
        private int amount_settle;
        private String currency;
        private String subject;
        private String body;
        private ExtraBean extra;
        private Object time_paid;
        private int time_expire;
        private Object time_settle;
        private Object transaction_no;
        private RefundsBean refunds;
        private int amount_refunded;
        private Object failure_code;
        private Object failure_msg;
        private MetadataBean metadata;
        private CredentialBean credential;
        private Object description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public int getCreated() {
            return created;
        }

        public void setCreated(int created) {
            this.created = created;
        }

        public boolean isLivemode() {
            return livemode;
        }

        public void setLivemode(boolean livemode) {
            this.livemode = livemode;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        public boolean isRefunded() {
            return refunded;
        }

        public void setRefunded(boolean refunded) {
            this.refunded = refunded;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getClient_ip() {
            return client_ip;
        }

        public void setClient_ip(String client_ip) {
            this.client_ip = client_ip;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAmount_settle() {
            return amount_settle;
        }

        public void setAmount_settle(int amount_settle) {
            this.amount_settle = amount_settle;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public ExtraBean getExtra() {
            return extra;
        }

        public void setExtra(ExtraBean extra) {
            this.extra = extra;
        }

        public Object getTime_paid() {
            return time_paid;
        }

        public void setTime_paid(Object time_paid) {
            this.time_paid = time_paid;
        }

        public int getTime_expire() {
            return time_expire;
        }

        public void setTime_expire(int time_expire) {
            this.time_expire = time_expire;
        }

        public Object getTime_settle() {
            return time_settle;
        }

        public void setTime_settle(Object time_settle) {
            this.time_settle = time_settle;
        }

        public Object getTransaction_no() {
            return transaction_no;
        }

        public void setTransaction_no(Object transaction_no) {
            this.transaction_no = transaction_no;
        }

        public RefundsBean getRefunds() {
            return refunds;
        }

        public void setRefunds(RefundsBean refunds) {
            this.refunds = refunds;
        }

        public int getAmount_refunded() {
            return amount_refunded;
        }

        public void setAmount_refunded(int amount_refunded) {
            this.amount_refunded = amount_refunded;
        }

        public Object getFailure_code() {
            return failure_code;
        }

        public void setFailure_code(Object failure_code) {
            this.failure_code = failure_code;
        }

        public Object getFailure_msg() {
            return failure_msg;
        }

        public void setFailure_msg(Object failure_msg) {
            this.failure_msg = failure_msg;
        }

        public MetadataBean getMetadata() {
            return metadata;
        }

        public void setMetadata(MetadataBean metadata) {
            this.metadata = metadata;
        }

        public CredentialBean getCredential() {
            return credential;
        }

        public void setCredential(CredentialBean credential) {
            this.credential = credential;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public static class ExtraBean {
        }

        public static class RefundsBean {
        }

        public static class MetadataBean {
        }

        public static class CredentialBean {
            /**
             * object : credential
             * wx : {"appId":"wxp4ihq5y1sytowvjl","partnerId":"1238560201","prepayId":"1101000000161111unhwx1i1u5ggbdar","nonceStr":"b7a42472e7db8b89f870b69aa961a32d","timeStamp":1478848025,"packageValue":"Sign=WXPay","sign":"93985fe34315b751ce77222c540b73822a49f9de"}
             */

            private String object;
            private WxBean wx;

            public String getObject() {
                return object;
            }

            public void setObject(String object) {
                this.object = object;
            }

            public WxBean getWx() {
                return wx;
            }

            public void setWx(WxBean wx) {
                this.wx = wx;
            }

            public static class WxBean {
                /**
                 * appId : wxp4ihq5y1sytowvjl
                 * partnerId : 1238560201
                 * prepayId : 1101000000161111unhwx1i1u5ggbdar
                 * nonceStr : b7a42472e7db8b89f870b69aa961a32d
                 * timeStamp : 1478848025
                 * packageValue : Sign=WXPay
                 * sign : 93985fe34315b751ce77222c540b73822a49f9de
                 */

                private String appId;
                private String partnerId;
                private String prepayId;
                private String nonceStr;
                private int timeStamp;
                private String packageValue;
                private String sign;

                public String getAppId() {
                    return appId;
                }

                public void setAppId(String appId) {
                    this.appId = appId;
                }

                public String getPartnerId() {
                    return partnerId;
                }

                public void setPartnerId(String partnerId) {
                    this.partnerId = partnerId;
                }

                public String getPrepayId() {
                    return prepayId;
                }

                public void setPrepayId(String prepayId) {
                    this.prepayId = prepayId;
                }

                public String getNonceStr() {
                    return nonceStr;
                }

                public void setNonceStr(String nonceStr) {
                    this.nonceStr = nonceStr;
                }

                public int getTimeStamp() {
                    return timeStamp;
                }

                public void setTimeStamp(int timeStamp) {
                    this.timeStamp = timeStamp;
                }

                public String getPackageValue() {
                    return packageValue;
                }

                public void setPackageValue(String packageValue) {
                    this.packageValue = packageValue;
                }

                public String getSign() {
                    return sign;
                }

                public void setSign(String sign) {
                    this.sign = sign;
                }
            }
        }
    }
}
