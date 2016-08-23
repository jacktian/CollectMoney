package com.yzdsmart.Collectmoney.http.map;

import com.yzdsmart.Collectmoney.http.OkHttpClientUtils;
import com.yzdsmart.Collectmoney.http.Url;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YZD on 2016/4/27.
 */
public class BaiduRequestAdapter {
    public static BaiduRequestService mRequestService = null;

    public static BaiduRequestService getRequestService() {
        if (null == mRequestService) {

            Retrofit mRetrofit = new Retrofit.Builder().baseUrl(Url.BAIDU_POI_URL)
                    .client(OkHttpClientUtils.getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mRequestService = mRetrofit.create(BaiduRequestService.class);
        }
        return mRequestService;
    }
}
