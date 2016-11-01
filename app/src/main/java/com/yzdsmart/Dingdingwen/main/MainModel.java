package com.yzdsmart.Dingdingwen.main;

import com.yzdsmart.Dingdingwen.tecent_im.service.TLSService;

import tencent.tls.platform.TLSPwdLoginListener;

/**
 * Created by YZD on 2016/8/30.
 */
public class MainModel {

    private TLSService tlsService;

    public MainModel(TLSService tlsService) {
        this.tlsService = tlsService;
    }

    /**
     * TLS账号登陆
     *
     * @param userName
     * @param password
     * @param listener
     */
    void chatLogin(String userName, String password, TLSPwdLoginListener listener) {
        tlsService.TLSPwdLogin(userName, password, listener);
    }
}
