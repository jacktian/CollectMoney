package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/9/5.
 */
public class PublishTaskLog {
    private String BazaCode;//商铺编码（加密）
    private Integer TotalGold;//发布的总金币数
    private Integer OverGold;//剩余金币
    private String BeginTime;//开始日期
    private String EndTime;//结束日期
    private String CreateTime;//创建日期

    public PublishTaskLog() {
    }

    public PublishTaskLog(String bazaCode, Integer totalGold, Integer overGold, String beginTime, String endTime, String createTime) {
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

    public Integer getTotalGold() {
        return TotalGold;
    }

    public void setTotalGold(Integer totalGold) {
        TotalGold = totalGold;
    }

    public Integer getOverGold() {
        return OverGold;
    }

    public void setOverGold(Integer overGold) {
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
