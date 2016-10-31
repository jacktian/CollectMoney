package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/24.
 */

public class PersonalWithdrawLog {
    private String CustCode;//用户内码
    private Integer Gold;//兑换金币数
    private String Cash;//提现金额（人民币）
    private String TimeStr;//时间间隔
    private String CreateTime;//操作时间

    public PersonalWithdrawLog() {
    }

    public PersonalWithdrawLog(String custCode, Integer gold, String cash, String timeStr, String createTime) {
        CustCode = custCode;
        Gold = gold;
        Cash = cash;
        TimeStr = timeStr;
        CreateTime = createTime;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
    }

    public Integer getGold() {
        return Gold;
    }

    public void setGold(Integer gold) {
        Gold = gold;
    }

    public String getCash() {
        return Cash;
    }

    public void setCash(String cash) {
        Cash = cash;
    }

    public String getTimeStr() {
        return TimeStr;
    }

    public void setTimeStr(String timeStr) {
        TimeStr = timeStr;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    @Override
    public String toString() {
        return "{" +
                "CustCode:'" + CustCode + '\'' +
                ", Gold:" + Gold +
                ", Cash:'" + Cash + '\'' +
                ", TimeStr:'" + TimeStr + '\'' +
                ", CreateTime:'" + CreateTime + '\'' +
                '}';
    }
}
