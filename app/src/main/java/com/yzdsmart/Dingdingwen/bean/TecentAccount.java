package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2016/8/27.
 */
public class TecentAccount {
    private String TCAccount;
    private String TCPassword;

    public TecentAccount() {
    }

    public TecentAccount(String TCAccount, String TCPassword) {
        this.TCAccount = TCAccount;
        this.TCPassword = TCPassword;
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
                "TCAccount:'" + TCAccount + '\'' +
                ", TCPassword:'" + TCPassword + '\'' +
                '}';
    }
}
