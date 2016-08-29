package com.yzdsmart.Collectmoney.http.response;

/**
 * Created by YZD on 2016/8/29.
 */
public class CustDetailInfoRequestResponse {
    private String C_Code;//用户内码
    private String C_UserCode;//用户名
    private String CName;//用户名
    private String CSex;
    private String CBirthday;
    private String CTel;
    private String CIdNo;
    private String CNation;
    private Double CHeight;
    private Double CWeight;
    private String CProfession;
    private String CAddress;
    private String CProv;
    private String CCity;
    private String CDist;
    private String CCountry;
    private String CRemark;
    private String TCAccount;
    private String TCPassword;

    public CustDetailInfoRequestResponse() {
    }

    public CustDetailInfoRequestResponse(String c_Code, String c_UserCode, String CName, String CSex, String CBirthday, String CTel, String CIdNo, String CNation, Double CHeight, Double CWeight, String CProfession, String CAddress, String CProv, String CCity, String CDist, String CCountry, String CRemark, String TCAccount, String TCPassword) {
        C_Code = c_Code;
        C_UserCode = c_UserCode;
        this.CName = CName;
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
        this.TCAccount = TCAccount;
        this.TCPassword = TCPassword;
    }

    public String getC_Code() {
        return C_Code;
    }

    public void setC_Code(String c_Code) {
        C_Code = c_Code;
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

    public String getTCAccount() {
        return TCAccount;
    }

    public void setTCAccount(String TCAccount) {
        this.TCAccount = TCAccount;
    }

    public String getTCPassword() {
        return TCPassword;
    }

    public void setTCPassword(String TCPassword) {
        this.TCPassword = TCPassword;
    }

    @Override
    public String toString() {
        return "{" +
                "C_Code:'" + C_Code + '\'' +
                ", C_UserCode:'" + C_UserCode + '\'' +
                ", CName:'" + CName + '\'' +
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
                ", TCAccount:'" + TCAccount + '\'' +
                ", TCPassword:'" + TCPassword + '\'' +
                '}';
    }
}
