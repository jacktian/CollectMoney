package com.yzdsmart.Dingdingwen.http.response;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by YZD on 2016/12/12.
 */

public class ValidateBankCardRequestResponse {
    private String bank;//银行代码
    private String bankname;//银行名称
    private Boolean validated;//验证结果
    private String cardType;//卡类型
    private String key;//卡号
    private JSONArray messages;
    private String stat;//执行结果

    public ValidateBankCardRequestResponse() {
    }

    public ValidateBankCardRequestResponse(String bank, String bankname, Boolean validated, String cardType, String key, JSONArray messages, String stat) {
        this.bank = bank;
        this.bankname = bankname;
        this.validated = validated;
        this.cardType = cardType;
        this.key = key;
        this.messages = messages;
        this.stat = stat;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JSONArray getMessages() {
        return messages;
    }

    public void setMessages(JSONArray messages) {
        this.messages = messages;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
