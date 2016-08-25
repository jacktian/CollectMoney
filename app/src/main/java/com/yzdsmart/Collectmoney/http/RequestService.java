package com.yzdsmart.Collectmoney.http;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by YZD on 2016/4/27.
 */
public interface RequestService {
    @GET(Url.IS_USER_EXIST)
    Observable<String> isUserExist(@Query("tel") Integer telNum);

//    /**
//     * @param userId
//     * @param portrait  头像Base64码
//     * @param authToken
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(Url.UPLOAD_PORTRAIT)
//    Observable<String> uploadPortrait(@Field("userId") Integer userId, @Field("portrait") String portrait, @Field("Auth_Token") String authToken);

}
