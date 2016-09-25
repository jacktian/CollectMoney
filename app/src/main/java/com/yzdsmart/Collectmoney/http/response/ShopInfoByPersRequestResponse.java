package com.yzdsmart.Collectmoney.http.response;

import java.util.List;

/**
 * Created by YZD on 2016/8/28.
 */
public class ShopInfoByPersRequestResponse {

    private String Code;//商铺编码
    private String Name;//商铺名称
    private String Pers;//联系人
    private String Tel;//联系电话
    private String Addr;//地址
    private String Remark;//备注
    private String Coor; //坐标
    private Integer AtteNum;//关注人次
    private Integer TotalGlodNum;//商铺剩余总金币数量
    private Integer VisiNum;//访问总人次
    private List<String> ImageLists;

    public ShopInfoByPersRequestResponse() {
    }

    public ShopInfoByPersRequestResponse(String code, String name, String pers, String tel, String addr, String remark, String coor, Integer atteNum, Integer totalGlodNum, Integer visiNum, List<String> imageLists) {
        Code = code;
        Name = name;
        Pers = pers;
        Tel = tel;
        Addr = addr;
        Remark = remark;
        Coor = coor;
        AtteNum = atteNum;
        TotalGlodNum = totalGlodNum;
        VisiNum = visiNum;
        ImageLists = imageLists;
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

    public Integer getAtteNum() {
        return AtteNum;
    }

    public void setAtteNum(Integer atteNum) {
        AtteNum = atteNum;
    }

    public Integer getTotalGlodNum() {
        return TotalGlodNum;
    }

    public void setTotalGlodNum(Integer totalGlodNum) {
        TotalGlodNum = totalGlodNum;
    }

    public Integer getVisiNum() {
        return VisiNum;
    }

    public void setVisiNum(Integer visiNum) {
        VisiNum = visiNum;
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
                ", AtteNum:" + AtteNum +
                ", TotalGlodNum:" + TotalGlodNum +
                ", VisiNum:" + VisiNum +
                ", ImageLists:" + ImageLists +
                '}';
    }
}
