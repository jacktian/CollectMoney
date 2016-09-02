package com.yzdsmart.Collectmoney.http.response;

/**
 * Created by YZD on 2016/9/2.
 */
public class PersonRequestResponse {
    private String CustCode;//用户内码
    private String CustName;//用户姓名
    private String Coor;//所在坐标

    public PersonRequestResponse() {
    }

    public PersonRequestResponse(String custCode, String custName, String coor) {
        CustCode = custCode;
        CustName = custName;
        Coor = coor;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getCoor() {
        return Coor;
    }

    public void setCoor(String coor) {
        Coor = coor;
    }

    @Override
    public String toString() {
        return "{" +
                "CustCode:'" + CustCode + '\'' +
                ", CustName:'" + CustName + '\'' +
                ", Coor:'" + Coor + '\'' +
                '}';
    }
}
