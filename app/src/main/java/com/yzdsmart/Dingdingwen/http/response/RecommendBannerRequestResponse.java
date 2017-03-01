package com.yzdsmart.Dingdingwen.http.response;

import java.util.List;

/**
 * Created by jacks on 2017/3/1.
 */

public class RecommendBannerRequestResponse {

    /**
     * lists : [{"Id":3,"DiscoverTitle":"深度好文：你才30多岁，你在害怕什么？","FileUrl":"http://139.196.177.114:7223//upload/DisCoverText/2017/2/14/20170214104235657.html","ImageUrl":"http://139.196.177.114:7223//upload/DisCoverLogo/2017/2/14/20170214104233273.jpeg","CreateTime":"2017-02-14 10:42:35"}]
     * lastsequence : 3
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     */

    private int lastsequence;
    private String ActionStatus;
    private int ErrorCode;
    private String ErrorInfo;
    private List<ListsBean> lists;

    public int getLastsequence() {
        return lastsequence;
    }

    public void setLastsequence(int lastsequence) {
        this.lastsequence = lastsequence;
    }

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

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * Id : 3
         * DiscoverTitle : 深度好文：你才30多岁，你在害怕什么？
         * FileUrl : http://139.196.177.114:7223//upload/DisCoverText/2017/2/14/20170214104235657.html
         * ImageUrl : http://139.196.177.114:7223//upload/DisCoverLogo/2017/2/14/20170214104233273.jpeg
         * CreateTime : 2017-02-14 10:42:35
         */

        private int Id;
        private String DiscoverTitle;
        private String FileUrl;
        private String ImageUrl;
        private String CreateTime;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getDiscoverTitle() {
            return DiscoverTitle;
        }

        public void setDiscoverTitle(String DiscoverTitle) {
            this.DiscoverTitle = DiscoverTitle;
        }

        public String getFileUrl() {
            return FileUrl;
        }

        public void setFileUrl(String FileUrl) {
            this.FileUrl = FileUrl;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }
    }
}
