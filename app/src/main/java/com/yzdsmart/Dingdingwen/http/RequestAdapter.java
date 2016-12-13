package com.yzdsmart.Dingdingwen.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YZD on 2016/4/27.
 */
public class RequestAdapter {
    public static RequestService mDDWRequestService = null;

    public static RequestService getDDWRequestService() {
        if (null == mDDWRequestService) {
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit mRetrofit = new Retrofit.Builder().baseUrl(Url.BASE_URL)
                    .client(OkHttpClientUtils.getOkHttpClient())
                    .addConverterFactory(new StringConverterFactory())
                    .addConverterFactory(LenientGsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mDDWRequestService = mRetrofit.create(RequestService.class);
        }
        return mDDWRequestService;
    }
}
