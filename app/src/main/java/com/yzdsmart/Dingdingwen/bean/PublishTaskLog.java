package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/5.
 */
public class PublishTaskLog {
    private String BazaCode;//商铺编码（加密）
    private Float TotalGold;//发布的总金币数
    private Float OverGold;//剩余金币
    private String BeginTime;//开始日期
    private String EndTime;//结束日期
    private String CreateTime;//创建日期

    public PublishTaskLog() {
    }

    public PublishTaskLog(String bazaCode, Float totalGold, Float overGold, String beginTime, String endTime, String createTime) {
        BazaCode = bazaCode;
        TotalGold = totalGold;
        OverGold = overGold;
        BeginTime = beginTime;
        EndTime = endTime;
        CreateTime = createTime;
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public Float getTotalGold() {
        return TotalGold;
    }

    public void setTotalGold(Float totalGold) {
        TotalGold = totalGold;
    }

    public Float getOverGold() {
        return OverGold;
    }

    public void setOverGold(Float overGold) {
        OverGold = overGold;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
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
                "BazaCode:'" + BazaCode + '\'' +
                ", TotalGold:" + TotalGold +
                ", OverGold:" + OverGold +
                ", BeginTime:'" + BeginTime + '\'' +
                ", EndTime:'" + EndTime + '\'' +
                ", CreateTime:'" + CreateTime + '\'' +
                '}';
    }
}
