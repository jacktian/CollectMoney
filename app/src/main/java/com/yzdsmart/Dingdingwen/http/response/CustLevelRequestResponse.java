package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/8/28.
 */
public class CustLevelRequestResponse {
    private String C_Code;//用户内码
    private String C_UserCode;//用户名
    private Integer Gra;//等级
    private Integer Sta;//星级

    public CustLevelRequestResponse() {
    }

    public CustLevelRequestResponse(String c_Code, String c_UserCode, Integer gra, Integer sta) {
        C_Code = c_Code;
        C_UserCode = c_UserCode;
        Gra = gra;
        Sta = sta;
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

    public Integer getGra() {
        return Gra;
    }

    public void setGra(Integer gra) {
        Gra = gra;
    }

    public Integer getSta() {
        return Sta;
    }

    public void setSta(Integer sta) {
        Sta = sta;
    }

    @Override
    public String toString() {
        return "{" +
                "C_Code:'" + C_Code + '\'' +
                ", C_UserCode:'" + C_UserCode + '\'' +
                ", Gra:" + Gra +
                ", Sta:" + Sta +
                '}';
    }
}
