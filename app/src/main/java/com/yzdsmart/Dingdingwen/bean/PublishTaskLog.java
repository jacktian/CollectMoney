package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/5.
 */
public class PublishTaskLog {
    private String BazaCode;//商铺编码（加密）
    private Double TotalGold;//发布的总金币数
    private Double OverGold;//剩余金币
    private String BeginTime;//开始日期
    private String EndTime;//结束日期
    private String CreateTime;//创建日期

    public PublishTaskLog() {
    }

    public String getBazaCode() {
        return BazaCode;
    }

    public void setBazaCode(String bazaCode) {
        BazaCode = bazaCode;
    }

    public Double getTotalGold() {
        return TotalGold;
    }

    public void setTotalGold(Double totalGold) {
        TotalGold = totalGold;
    }

    public Double getOverGold() {
        return OverGold;
    }

    public void setOverGold(Double overGold) {
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
