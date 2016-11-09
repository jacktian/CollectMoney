package com.yzdsmart.Dingdingwen.http.response;

/**
 * Created by YZD on 2016/11/9.
 */

public class GetTokenRequestResponse {

    /**
     * access_token : UwHVVLcuVdp1t4E9GJjKU843IPqPLU7uy1kF2V9shJz0_YIH7auOd5psEBNbUfqxVfG8qFX0k4m-P8e-v9W60weT_ELpqbQK18GY-A_LixmQ5Cwf6_pRxkMmLd5k-vwWmyYqpYNd4XcwabOn3KHQZRxbTPnF3kgdJahFIEFnjMBeLUyrEIrZj8V6BbA2J-GTb0i7nHAHbBj73ZBaGIKoe2wN--M33eRmQnr0dyEgFfauYXahH-P0i8691TmHF84krtS-8olJfkC8ePyotM1TUMZQXT9e35JfY9MymF1ZGaPmlp3oSUzufLAFFoLaNLZ8wajc-XEhfzxMxivs_GXbCg
     * token_type : bearer
     * expires_in : 9
     * refresh_token : a4717833cabc42cfba8ed7f63e32def5
     * userName : 13196739736
     */

    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private String userName;

    public GetTokenRequestResponse() {
    }

    public GetTokenRequestResponse(String access_token, String token_type, int expires_in, String refresh_token, String userName) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.userName = userName;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "{" +
                "access_token:'" + access_token + '\'' +
                ", token_type:'" + token_type + '\'' +
                ", expires_in:" + expires_in +
                ", refresh_token:'" + refresh_token + '\'' +
                ", userName:'" + userName + '\'' +
                '}';
    }
}
