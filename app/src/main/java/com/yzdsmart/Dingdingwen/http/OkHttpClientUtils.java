package com.yzdsmart.Dingdingwen.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by YZD on 2016/4/27.
 */
public class OkHttpClientUtils {
    private final static Integer DEFAULT_TIMEOUT = 60;
    public static OkHttpClient mOkHttpClient = null;

    public static OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            //配置Http请求的Headers
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Accept", "*/*")
                            .build();
                    return chain.proceed(request);
                }
            };
            //打印网络请求日志
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        }
        return mOkHttpClient;
    }
}
