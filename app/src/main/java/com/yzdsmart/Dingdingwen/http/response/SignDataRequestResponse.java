package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2017/1/20.
 */

public class SignDataRequestResponse {


    /**
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     * Data : {"ActiName":"第一个活动","ActiRoute":"一号点位,二号点位,三号点位,四号点位","GoRoute":"一号点位","FirstDateTime":"2017-01-20 14:41:10","LastDateTime":"2017-01-20 14:41:10"}
     */

    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private DataBean Data;

    public String getActionStatus() {
        return ActionStatus;
    }

    public void setActionStatus(String ActionStatus) {
        this.ActionStatus = ActionStatus;
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

    public void setErrorInfo(String ErrorInfo) {
        this.ErrorInfo = ErrorInfo;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * ActiName : 第一个活动
         * ActiRoute : 一号点位,二号点位,三号点位,四号点位
         * GoRoute : 一号点位
         * FirstDateTime : 2017-01-20 14:41:10
         * LastDateTime : 2017-01-20 14:41:10
         */

        private String ActiName;
        private String ActiRoute;
        private String GoRoute;
        private String FirstDateTime;
        private String LastDateTime;

        public String getActiName() {
            return ActiName;
        }

        public void setActiName(String ActiName) {
            this.ActiName = ActiName;
        }

        public String getActiRoute() {
            return ActiRoute;
        }

        public void setActiRoute(String ActiRoute) {
            this.ActiRoute = ActiRoute;
        }

        public String getGoRoute() {
            return GoRoute;
        }

        public void setGoRoute(String GoRoute) {
            this.GoRoute = GoRoute;
        }

        public String getFirstDateTime() {
            return FirstDateTime;
        }

        public void setFirstDateTime(String FirstDateTime) {
            this.FirstDateTime = FirstDateTime;
        }

        public String getLastDateTime() {
            return LastDateTime;
        }

        public void setLastDateTime(String LastDateTime) {
            this.LastDateTime = LastDateTime;
        }
    }
}
