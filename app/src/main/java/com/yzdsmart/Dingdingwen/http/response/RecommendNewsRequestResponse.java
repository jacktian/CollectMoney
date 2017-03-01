package com.yzdsmart.Dingdingwen.http.response;

import java.util.List;

/**
 * Created by jacks on 2017/3/1.
 */

public class RecommendNewsRequestResponse {

    /**
     * lists : [{"Id":7,"DiscoverTitle":"巧了！偷拍闺蜜吃相，意外拍下扒手盗窃瞬间","DiscoverContent":"巧了！偷拍闺蜜吃相，意外拍下扒手盗窃瞬间","FileUrl":"http://139.196.177.114:7223//upload/DisCoverText/2017/2/14/20170214105412587.html","ImageUrl":"http://139.196.177.114:7223//upload/DisCoverLogo/2017/2/14/20170214105328998.jpeg","CreateTime":"2017-02-14 10:54:12"},{"Id":5,"DiscoverTitle":"毛衣10分钟、羽绒服20分钟就能速干，\u201c神器\u201d家家都有！","DiscoverContent":"冬天就要过去了，又要开始清洗厚厚的冬装了。最近温度低，还连续阴雨，衣服晾不干怎么办？教你一招速干法，连烘干机都不用，就能轻松弄干衣服，连最难晾干的羽绒服和毛衣都可以！","FileUrl":"http://139.196.177.114:7223//upload/DisCoverText/2017/2/14/20170214104748109.html","ImageUrl":"http://139.196.177.114:7223//upload/DisCoverLogo/2017/2/14/20170214104621856.jpeg","CreateTime":"2017-02-14 10:47:48"},{"Id":4,"DiscoverTitle":"2017朋友圈里最动人的照片，你看到第几张泪奔了？","DiscoverContent":"　人生百态，总有百般滋味。有太多事与愿违，也有太多的心酸不忍言说，但同时也有很多善意的瞬间，让你有了继续生活下去的勇气和希望。\r\n\r\n　　一张照片胜过千言万语，总有一张让你心中一软，泪从中来。","FileUrl":"http://139.196.177.114:7223//upload/DisCoverText/2017/2/14/20170214104433874.html","ImageUrl":"http://139.196.177.114:7223//upload/DisCoverLogo/2017/2/14/20170214104356024.jpeg","CreateTime":"2017-02-14 10:44:33"},{"Id":3,"DiscoverTitle":"深度好文：你才30多岁，你在害怕什么？","DiscoverContent":"深度好文：你才30多岁，你在害怕什么？","FileUrl":"http://139.196.177.114:7223//upload/DisCoverText/2017/2/14/20170214104235657.html","ImageUrl":"http://139.196.177.114:7223//upload/DisCoverLogo/2017/2/14/20170214104233273.jpeg","CreateTime":"2017-02-14 10:42:35"},{"Id":2,"DiscoverTitle":"一周精选","DiscoverContent":"内容精选","FileUrl":"http://139.196.177.114:7223//upload/DisCoverText/2017/2/14/20170214103637006.html","ImageUrl":"http://139.196.177.114:7223//upload/DisCoverLogo/2017/2/14/20170214103625225.jpeg","CreateTime":"2017-02-14 10:36:37"}]
     * lastsequence : 7
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
         * Id : 7
         * DiscoverTitle : 巧了！偷拍闺蜜吃相，意外拍下扒手盗窃瞬间
         * DiscoverContent : 巧了！偷拍闺蜜吃相，意外拍下扒手盗窃瞬间
         * FileUrl : http://139.196.177.114:7223//upload/DisCoverText/2017/2/14/20170214105412587.html
         * ImageUrl : http://139.196.177.114:7223//upload/DisCoverLogo/2017/2/14/20170214105328998.jpeg
         * CreateTime : 2017-02-14 10:54:12
         */

        private int Id;
        private String DiscoverTitle;
        private String DiscoverContent;
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

        public String getDiscoverContent() {
            return DiscoverContent;
        }

        public void setDiscoverContent(String DiscoverContent) {
            this.DiscoverContent = DiscoverContent;
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
