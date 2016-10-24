package com.yzdsmart.Collectmoney.http;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by YZD on 2016/4/27.
 */
public interface PingPPService {
    @POST
    Observable<String> orderPay(@Query("code") String code, @Query("submitcode") String submitcode, @Query("action") String action);
}
