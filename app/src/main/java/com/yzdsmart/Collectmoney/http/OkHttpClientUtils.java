package com.yzdsmart.Collectmoney.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;


/**
 * Created by YZD on 2016/4/27.
 */
public class OkHttpClientUtils {
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
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return mOkHttpClient;
    }

    /**
     * 拦截器压缩http请求体，许多服务器无法解析
     */
    static class GzipRequestInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }
            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public long contentLength() {
                    return -1; // 无法知道压缩后的数据大小
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }
}
