package com.yzdsmart.Collectmoney.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YZD on 2016/4/27.
 */
public class PingPPAdapter {
    public static PingPPService mPingPPService = null;

    public static PingPPService getPingPPService() {
        if (null == mPingPPService) {
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit mRetrofit = new Retrofit.Builder().baseUrl(Url.PING_PLUS_PLUS_URL)
                    .client(OkHttpClientUtils.getOkHttpClient())
                    .addConverterFactory(new StringConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mPingPPService = mRetrofit.create(PingPPService.class);
        }
        return mPingPPService;
    }
}
