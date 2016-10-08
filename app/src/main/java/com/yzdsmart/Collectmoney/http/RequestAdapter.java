package com.yzdsmart.Collectmoney.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YZD on 2016/4/27.
 */
public class RequestAdapter {
    public static RequestService mRequestService = null;

    public static RequestService getRequestService() {
        if (null == mRequestService) {
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit mRetrofit = new Retrofit.Builder().baseUrl(Url.BASE_URL)
                    .client(OkHttpClientUtils.getOkHttpClient())
//                    .addConverterFactory(new StringConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mRequestService = mRetrofit.create(RequestService.class);
        }
        return mRequestService;
    }
}
