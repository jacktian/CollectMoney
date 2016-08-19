package com.yzdsmart.Collectmoney.bean;

/**
 * Created by YZD on 2016/8/19.
 */
public class TouristAttraction {
    private String attractionImgUrl;
    private String attractionName;

    public TouristAttraction() {
    }

    public TouristAttraction(String attractionImgUrl, String attractionName) {
        this.attractionImgUrl = attractionImgUrl;
        this.attractionName = attractionName;
    }

    public String getAttractionImgUrl() {
        return attractionImgUrl;
    }

    public void setAttractionImgUrl(String attractionImgUrl) {
        this.attractionImgUrl = attractionImgUrl;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    @Override
    public String toString() {
        return "{" +
                "attractionImgUrl:'" + attractionImgUrl + '\'' +
                ", attractionName:'" + attractionName + '\'' +
                '}';
    }
}
