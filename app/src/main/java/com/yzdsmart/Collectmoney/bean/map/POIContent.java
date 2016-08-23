package com.yzdsmart.Collectmoney.bean.map;

import java.util.List;

/**
 * Created by YZD on 2016/7/5.
 */
public class POIContent {
    private Integer status;
    private Integer total;
    private Integer size;
    private List<GEOContents> contents;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<GEOContents> getContents() {
        return contents;
    }

    public void setContents(List<GEOContents> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "{" +
                "status:" + status +
                ", total:" + total +
                ", size:" + size +
                ", contents:" + contents +
                '}';
    }
}
