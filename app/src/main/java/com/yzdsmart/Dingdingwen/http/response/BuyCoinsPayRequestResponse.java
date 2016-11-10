package com.yzdsmart.Dingdingwen.http.response;

import java.util.List;

/**
 * Created by YZD on 2016/10/27.
 */

public class BuyCoinsPayRequestResponse {

    /**
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     * Charge : {"id":"ch_zDCur5yPizHKennTC4jLCSC0","object":"charge","created":1477470170,"livemode":false,"paid":false,"refunded":false,"app":"app_1Gqj58ynP0mHeX1q","channel":"wx","order_no":"1222221","client_ip":"127.0.0.1","amount":3,"amount_settle":3,"currency":"cny","subject":"test","body":"121212","extra":{},"time_paid":null,"time_expire":1477477370,"time_settle":null,"transaction_no":null,"refunds":{"object":"list","url":"/v1/charges/ch_zDCur5yPizHKennTC4jLCSC0/refunds","has_more":false,"data":[]},"amount_refunded":0,"failure_code":null,"failure_msg":null,"metadata":{},"credential":{"object":"credential","wx":{"appId":"wxmdq1co8ckmhsgw1g","partnerId":"1223185601","prepayId":"1101000000161026os4yl0satmpkcux1","nonceStr":"c3990e5318b0fec63e2a9aaa2270b71c","timeStamp":1477470170,"packageValue":"Sign=WXPay","sign":"221ec51f9db548189313a109ee1f36057ae19db2"}},"description":null}
     */

    private String ActionStatus;
    private int ErrorCode;
    private String ErrorInfo;
    /**
     * id : ch_zDCur5yPizHKennTC4jLCSC0
     * object : charge
     * created : 1477470170
     * livemode : false
     * paid : false
     * refunded : false
     * app : app_1Gqj58ynP0mHeX1q
     * channel : wx
     * order_no : 1222221
     * client_ip : 127.0.0.1
     * amount : 3
     * amount_settle : 3
     * currency : cny
     * subject : test
     * body : 121212
     * extra : {}
     * time_paid : null
     * time_expire : 1477477370
     * time_settle : null
     * transaction_no : null
     * refunds : {"object":"list","url":"/v1/charges/ch_zDCur5yPizHKennTC4jLCSC0/refunds","has_more":false,"data":[]}
     * amount_refunded : 0
     * failure_code : null
     * failure_msg : null
     * metadata : {}
     * credential : {"object":"credential","wx":{"appId":"wxmdq1co8ckmhsgw1g","partnerId":"1223185601","prepayId":"1101000000161026os4yl0satmpkcux1","nonceStr":"c3990e5318b0fec63e2a9aaa2270b71c","timeStamp":1477470170,"packageValue":"Sign=WXPay","sign":"221ec51f9db548189313a109ee1f36057ae19db2"}}
     * description : null
     */

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
        private Object time_paid;
        private int time_expire;
        private Object time_settle;
        private Object transaction_no;
        /**
         * object : list
         * url : /v1/charges/ch_zDCur5yPizHKennTC4jLCSC0/refunds
         * has_more : false
         * data : []
         */

        private RefundsBean refunds;
        private int amount_refunded;
        private Object failure_code;
        private Object failure_msg;
        /**
         * object : credential
         * wx : {"appId":"wxmdq1co8ckmhsgw1g","partnerId":"1223185601","prepayId":"1101000000161026os4yl0satmpkcux1","nonceStr":"c3990e5318b0fec63e2a9aaa2270b71c","timeStamp":1477470170,"packageValue":"Sign=WXPay","sign":"221ec51f9db548189313a109ee1f36057ae19db2"}
         */

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

        public static class RefundsBean {
            private String object;
            private String url;
            private boolean has_more;
            private List<?> data;

            public String getObject() {
                return object;
            }

            public void setObject(String object) {
                this.object = object;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public boolean isHas_more() {
                return has_more;
            }

            public void setHas_more(boolean has_more) {
                this.has_more = has_more;
            }

            public List<?> getData() {
                return data;
            }

            public void setData(List<?> data) {
                this.data = data;
            }
        }

        public static class CredentialBean {
            private String object;
            /**
             * appId : wxmdq1co8ckmhsgw1g
             * partnerId : 1223185601
             * prepayId : 1101000000161026os4yl0satmpkcux1
             * nonceStr : c3990e5318b0fec63e2a9aaa2270b71c
             * timeStamp : 1477470170
             * packageValue : Sign=WXPay
             * sign : 221ec51f9db548189313a109ee1f36057ae19db2
             */

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