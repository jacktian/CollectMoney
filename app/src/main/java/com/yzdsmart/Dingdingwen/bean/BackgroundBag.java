package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/12/2.
 */

public class BackgroundBag {
    private Integer type;//0 金币 1 砖石
    private Integer counts;

    public BackgroundBag() {
    }

    public BackgroundBag(Integer type, Integer counts) {
        this.type = type;
        this.counts = counts;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }
}
