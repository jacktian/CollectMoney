package com.yzdsmart.Dingdingwen.http.response;

import java.util.List;

/**
 * Created by YZD on 2017/3/8.
 */

public class MarketsInfoRequestResponse {

    /**
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     * Data : [{"ComplexName":"江南环球港","StoreyList":["B2层","B1层","一楼","二楼","三楼","四楼","五楼"]}]
     */

    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private List<DataBean> Data;

    public String getActionStatus() {
        return ActionStatus;
    }

    public void setActionStatus(String actionStatus) {
        ActionStatus = actionStatus;
    }

    public Integer getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(Integer errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        ErrorInfo = errorInfo;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> data) {
        Data = data;
    }

    public static class DataBean {
        /**
         * ComplexName : 江南环球港
         * StoreyList : ["B2层","B1层","一楼","二楼","三楼","四楼","五楼"]
         */

        private String ComplexName;
        private List<String> StoreyList;

        public String getComplexName() {
            return ComplexName;
        }

        public void setComplexName(String ComplexName) {
            this.ComplexName = ComplexName;
        }

        public List<String> getStoreyList() {
            return StoreyList;
        }

        public void setStoreyList(List<String> StoreyList) {
            this.StoreyList = StoreyList;
        }
    }
}
