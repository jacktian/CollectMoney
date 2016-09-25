package com.yzdsmart.Collectmoney.http.response;

/**
 * Created by YZD on 2016/8/29.
 */
public class CustDetailInfoRequestResponse {
    private String C_UserCode;//用户名
    private String CName;//用户姓名
    private String CNickName;//用户昵称
    private String CSex;//性别
    private String CBirthday;//生日
    private String CTel;//手机
    private String CIdNo;//身份证
    private String CNation;//民族
    private Double CHeight;//身高
    private Double CWeight;//体重
    private String CProfession;//职业
    private String CAddress;//地址
    private String CProv;//省
    private String CCity;//市
    private String CDist;//区
    private String CCountry;//国家
    private String CRemark;//备注

    public CustDetailInfoRequestResponse() {
    }

    public CustDetailInfoRequestResponse(String c_UserCode, String CName, String CNickName, String CSex, String CBirthday, String CTel, String CIdNo, String CNation, Double CHeight, Double CWeight, String CProfession, String CAddress, String CProv, String CCity, String CDist, String CCountry, String CRemark) {
        C_UserCode = c_UserCode;
        this.CName = CName;
        this.CNickName = CNickName;
        this.CSex = CSex;
        this.CBirthday = CBirthday;
        this.CTel = CTel;
        this.CIdNo = CIdNo;
        this.CNation = CNation;
        this.CHeight = CHeight;
        this.CWeight = CWeight;
        this.CProfession = CProfession;
        this.CAddress = CAddress;
        this.CProv = CProv;
        this.CCity = CCity;
        this.CDist = CDist;
        this.CCountry = CCountry;
        this.CRemark = CRemark;
    }

    public String getC_UserCode() {
        return C_UserCode;
    }

    public void setC_UserCode(String c_UserCode) {
        C_UserCode = c_UserCode;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getCNickName() {
        return CNickName;
    }

    public void setCNickName(String CNickName) {
        this.CNickName = CNickName;
    }

    public String getCSex() {
        return CSex;
    }

    public void setCSex(String CSex) {
        this.CSex = CSex;
    }

    public String getCBirthday() {
        return CBirthday;
    }

    public void setCBirthday(String CBirthday) {
        this.CBirthday = CBirthday;
    }

    public String getCTel() {
        return CTel;
    }

    public void setCTel(String CTel) {
        this.CTel = CTel;
    }

    public String getCIdNo() {
        return CIdNo;
    }

    public void setCIdNo(String CIdNo) {
        this.CIdNo = CIdNo;
    }

    public String getCNation() {
        return CNation;
    }

    public void setCNation(String CNation) {
        this.CNation = CNation;
    }

    public Double getCHeight() {
        return CHeight;
    }

    public void setCHeight(Double CHeight) {
        this.CHeight = CHeight;
    }

    public Double getCWeight() {
        return CWeight;
    }

    public void setCWeight(Double CWeight) {
        this.CWeight = CWeight;
    }

    public String getCProfession() {
        return CProfession;
    }

    public void setCProfession(String CProfession) {
        this.CProfession = CProfession;
    }

    public String getCAddress() {
        return CAddress;
    }

    public void setCAddress(String CAddress) {
        this.CAddress = CAddress;
    }

    public String getCProv() {
        return CProv;
    }

    public void setCProv(String CProv) {
        this.CProv = CProv;
    }

    public String getCCity() {
        return CCity;
    }

    public void setCCity(String CCity) {
        this.CCity = CCity;
    }

    public String getCDist() {
        return CDist;
    }

    public void setCDist(String CDist) {
        this.CDist = CDist;
    }

    public String getCCountry() {
        return CCountry;
    }

    public void setCCountry(String CCountry) {
        this.CCountry = CCountry;
    }

    public String getCRemark() {
        return CRemark;
    }

    public void setCRemark(String CRemark) {
        this.CRemark = CRemark;
    }

    @Override
    public String toString() {
        return "{" +
                "C_UserCode:'" + C_UserCode + '\'' +
                ", CName:'" + CName + '\'' +
                ", CNickName:'" + CNickName + '\'' +
                ", CSex:'" + CSex + '\'' +
                ", CBirthday:'" + CBirthday + '\'' +
                ", CTel:'" + CTel + '\'' +
                ", CIdNo:'" + CIdNo + '\'' +
                ", CNation:'" + CNation + '\'' +
                ", CHeight:" + CHeight +
                ", CWeight:" + CWeight +
                ", CProfession:'" + CProfession + '\'' +
                ", CAddress:'" + CAddress + '\'' +
                ", CProv:'" + CProv + '\'' +
                ", CCity:'" + CCity + '\'' +
                ", CDist:'" + CDist + '\'' +
                ", CCountry:'" + CCountry + '\'' +
                ", CRemark:'" + CRemark + '\'' +
                '}';
    }
}
