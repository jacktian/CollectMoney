package com.yzdsmart.Dingdingwen.http.response;

import java.util.List;

/**
 * Created by YZD on 2016/8/28.
 */
public class ShopInfoRequestResponse {

    private String Code;//商铺编码
    private String Name;//商铺名称
    private String Pers;//联系人
    private String Tel;//联系电话
    private String Addr;//地址
    private String Remark;//备注
    private String Coor; //坐标
    private Boolean IsAtte;//当前用户是否关注
    private Integer AtteNum;//关注人次
    private Double TodayGlodNum;//今日金币数量
    private Integer VisiNum;//访问总人次
    private String LogoImageUrl;//Logo图片链接
    private List<String> ImageLists;

    public ShopInfoRequestResponse() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPers() {
        return Pers;
    }

    public void setPers(String pers) {
        Pers = pers;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCoor() {
        return Coor;
    }

    public void setCoor(String coor) {
        Coor = coor;
    }

    public Boolean getAtte() {
        return IsAtte;
    }

    public void setAtte(Boolean atte) {
        IsAtte = atte;
    }

    public Integer getAtteNum() {
        return AtteNum;
    }

    public void setAtteNum(Integer atteNum) {
        AtteNum = atteNum;
    }

    public Double getTodayGlodNum() {
        return TodayGlodNum;
    }

    public void setTodayGlodNum(Double todayGlodNum) {
        TodayGlodNum = todayGlodNum;
    }

    public Integer getVisiNum() {
        return VisiNum;
    }

    public void setVisiNum(Integer visiNum) {
        VisiNum = visiNum;
    }

    public String getLogoImageUrl() {
        return LogoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        LogoImageUrl = logoImageUrl;
    }

    public List<String> getImageLists() {
        return ImageLists;
    }

    public void setImageLists(List<String> imageLists) {
        ImageLists = imageLists;
    }

    @Override
    public String toString() {
        return "{" +
                "Code:'" + Code + '\'' +
                ", Name:'" + Name + '\'' +
                ", Pers:'" + Pers + '\'' +
                ", Tel:'" + Tel + '\'' +
                ", Addr:'" + Addr + '\'' +
                ", Remark:'" + Remark + '\'' +
                ", Coor:'" + Coor + '\'' +
                ", IsAtte:" + IsAtte +
                ", AtteNum:" + AtteNum +
                ", TodayGlodNum:" + TodayGlodNum +
                ", VisiNum:" + VisiNum +
                ", LogoImageUrl:" + LogoImageUrl +
                ", ImageLists:" + ImageLists +
                '}';
    }
}
