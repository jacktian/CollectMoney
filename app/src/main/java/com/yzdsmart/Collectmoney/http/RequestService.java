package com.yzdsmart.Collectmoney.http;

import com.yzdsmart.Collectmoney.bean.RequestResponse;
import com.yzdsmart.Collectmoney.bean.Shop;
import com.yzdsmart.Collectmoney.bean.ShopDetails;

import java.util.List;

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
    /**
     * 校验手机号码是否已注册
     *
     * @param telNum
     * @return
     */
    @GET(Url.USER)
    Observable<RequestResponse> isUserExist(@Query("tel") String telNum);

    /**
     * 获取短信验证码
     *
     * @param telNum
     * @param currDate
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SMS)
    Observable<RequestResponse> getVerifyCode(@Field("Tel") String telNum, @Field("CurrDate") String currDate);

    /**
     * 验证短信验证码
     *
     * @param telNum
     * @param verifyCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SMS + "?actioncode=000000")
    Observable<RequestResponse> verifyVerifyCode(@Field("Tel") String telNum, @Field("Sms_Veri_Code") String verifyCode);

    /**
     * 用户注册/修改密码
     *
     * @param userName
     * @param password
     * @param regCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.USER + "?actioncode=000000")
    Observable<RequestResponse> setPassword(@Field("UserName") String userName, @Field("Password") String password, @Field("RegCode") String regCode);

    /**
     * 用户登录
     *
     * @param userName
     * @param password
     * @param loginCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.USER)
    Observable<RequestResponse> userLogin(@Field("UserName") String userName, @Field("Password") String password, @Field("LoginCode") String loginCode);

    /**
     * 获取周边商铺
     *
     * @param submitCode
     * @param coor
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOPLIST)
    Observable<List<Shop>> getShopList(@Field("SubmitCode") String submitCode, @Field("Coor") String coor, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

    /**
     * 获取商铺详情
     *
     * @param submitCode
     * @param bazaCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOPLIST + "?actioncode=000000")
    Observable<ShopDetails> getShopDetails(@Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode);
}
