package com.yzdsmart.Collectmoney.http;

import com.yzdsmart.Collectmoney.bean.FriendsRequestResponse;
import com.yzdsmart.Collectmoney.bean.LoginRequestResponse;
import com.yzdsmart.Collectmoney.bean.RequestResponse;
import com.yzdsmart.Collectmoney.bean.Shop;
import com.yzdsmart.Collectmoney.bean.ShopDetails;
import com.yzdsmart.Collectmoney.bean.User;

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
     * 用户注册/修改密码
     *
     * @param userName
     * @param password
     * @param regCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.USER)
    Observable<RequestResponse> setPassword(@Query("actioncode") String actioncode, @Field("UserName") String userName, @Field("Password") String password, @Field("RegCode") String regCode);

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
    Observable<LoginRequestResponse> userLogin(@Field("UserName") String userName, @Field("Password") String password, @Field("LoginCode") String loginCode);

    /**
     * 获取以后等级和星级
     *
     * @param custcode
     * @param submitcode
     * @return
     */
    @GET(Url.CUST)
    Observable<User> getUserGraSta(@Query("custcode") String custcode, @Query("submitcode") String submitcode);

    /**
     * 设置云通讯用户信息
     *
     * @param actioncode
     * @param submitCode
     * @param tcAccount
     * @param profile_Nick
     * @param profile_Image
     * @param profile_AllowType
     * @param profile_SelfSignature
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SETCUST)
    Observable<RequestResponse> setTecentInfo(@Query("actioncode") String actioncode, @Field("SubmitCode") String submitCode, @Field("TCAccount") String tcAccount, @Field("Profile_Nick") String profile_Nick, @Field("Profile_Image") String profile_Image, @Field("Profile_AllowType") String profile_AllowType, @Field("Profile_SelfSignature") String profile_SelfSignature);

    /**
     * 获取好友的列表(包括其等级和星级）
     *
     * @param submitCode
     * @param custCode
     * @param timeStampNow
     * @param startIndex
     * @param currentStandardSequence
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.FRIEND)
    Observable<FriendsRequestResponse> getFriendsList(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("TimeStampNow") Long timeStampNow, @Field("StartIndex") Integer startIndex, @Field("CurrentStandardSequence") Integer currentStandardSequence, @Field("PageSize") Integer pageSize);

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
    @POST(Url.SMS)
    Observable<RequestResponse> verifyVerifyCode(@Query("actioncode") String actioncode, @Field("Tel") String telNum, @Field("Sms_Veri_Code") String verifyCode);

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
    @POST(Url.SHOPLIST)
    Observable<ShopDetails> getShopDetails(@Query("actioncode") String actioncode, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode);

    /**
     * 设置对店铺关注
     *
     * @param submitCode
     * @param custCode
     * @param bazaCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SETFOLLOW)
    Observable<RequestResponse> changeShopFollow(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("BazaCode") String bazaCode);

}
