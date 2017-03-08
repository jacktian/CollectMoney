package com.yzdsmart.Dingdingwen.http;

import com.yzdsmart.Dingdingwen.bean.BankCard;
import com.yzdsmart.Dingdingwen.bean.MarketShop;
import com.yzdsmart.Dingdingwen.bean.ShopScanner;
import com.yzdsmart.Dingdingwen.http.response.BackgroundBagRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.BuyCoinsLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CoinTypeRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CouponLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustLevelRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ExpandListRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.FocusedShopRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.FriendsRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GameTaskRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetCoinsLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetTokenRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.LoginRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.MarketsInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PayRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PaymentLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PersonRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PersonalWithdrawLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PublishTaskLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RecommendBannerRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RecommendFriendsRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RecommendNewsRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RegisterBusinessRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ScanCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ScannedLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopDiscountRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopExchangeRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopFocuserRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoByPersRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopListRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopPayLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopTaskLeftCoinsRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopWithdrawLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.SignDataRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.UploadFileRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ValidateBankCardRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.WithdrawRequestResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by YZD on 2016/4/27.
 */
public interface RequestService {
    /**
     * 注册应用进行认证
     *
     * @param AppCode   app唯一编码
     * @param appId     app编号
     * @param appSecret app加密码
     * @return
     */
    @FormUrlEncoded
    @POST(Url.APP_REG)
    Observable<RequestResponse> appRegister(@Field("AppCode") String AppCode, @Field("AppId") String appId, @Field("AppSecret") String appSecret);

    /**
     * 获取refresh token 和 access token
     *
     * @param grantType
     * @param userName
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TOKEN)
    Observable<GetTokenRequestResponse> getRefreshToken(@Field("grant_type") String grantType, @Field("username") String userName, @Field("password") String password);

    /**
     * 刷新 access token
     *
     * @param grantType
     * @param refreshToken
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TOKEN)
    Observable<GetTokenRequestResponse> refreshAccessToken(@Field("grant_type") String grantType, @Field("refresh_token") String refreshToken);

    /**
     * 校验手机号码是否已注册
     *
     * @param telNum
     * @return
     */
    @GET(Url.USER)
    Observable<RequestResponse> isUserExist(@Query("tel") String telNum, @Header("Authorization") String authorization);

    /**
     * 获取短信验证码
     *
     * @param telNum
     * @param currDate
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SMS)
    Observable<RequestResponse> getVerifyCode(@Field("Tel") String telNum, @Field("CurrDate") String currDate, @Header("Authorization") String authorization);

    /**
     * 验证短信验证码
     *
     * @param telNum
     * @param verifyCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SMS)
    Observable<RequestResponse> validateVerifyCode(@Query("actioncode") String actioncode, @Field("Tel") String telNum, @Field("Sms_Veri_Code") String verifyCode, @Header("Authorization") String authorization);

    /**
     * 修改密码
     *
     * @param userName
     * @param password
     * @param regCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.USER)
    Observable<RequestResponse> setPassword(@Query("actioncode") String actioncode, @Field("UserName") String userName, @Field("Password") String password, @Field("RegCode") String regCode, @Header("Authorization") String authorization);

    /**
     * 用户手机注册信息
     *
     * @param actioncode
     * @param userName
     * @param password
     * @param cSex
     * @param cAge
     * @param cNickName
     * @param regCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.USER)
    Observable<RequestResponse> userRegister(@Query("actioncode") String actioncode, @Field("UserName") String userName, @Field("Password") String password, @Field("CSex") String cSex, @Field("CAge") Integer cAge, @Field("CNickName") String cNickName, @Field("RegCode") String regCode, @Header("Authorization") String authorization);

    /**
     * 第三方注册
     *
     * @param actioncode
     * @param userName
     * @param password
     * @param cSex
     * @param cAge
     * @param cNickName
     * @param otherElec
     * @return
     */
    @FormUrlEncoded
    @POST(Url.USER)
    Observable<LoginRequestResponse> thirdPlatformRegister(@Query("actioncode") String actioncode, @Field("UserName") String userName, @Field("Password") String password, @Field("CSex") String cSex, @Field("CAge") Integer cAge, @Field("CNickName") String cNickName, @Field("OtherElec") String otherElec, @Field("ElecInfo") String elecInfo, @Field("RegCode") String regCode, @Header("Authorization") String authorization);

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
    Observable<LoginRequestResponse> userLogin(@Field("UserName") String userName, @Field("Password") String password, @Field("LoginCode") String loginCode, @Header("Authorization") String authorization);

    /**
     * 第三方登录
     *
     * @param userName
     * @param otherElec
     * @param loginCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.USER)
    Observable<LoginRequestResponse> thirdPlatformLogin(@Field("UserName") String userName, @Field("OtherElec") String otherElec, @Field("LoginCode") String loginCode, @Header("Authorization") String authorization);

    /**
     * 获取用户等级和星级
     *
     * @param code
     * @param submitcode
     * @param action
     * @return
     */
    @GET(Url.CUST)
    Observable<CustLevelRequestResponse> getCustLevel(@Query("code") String code, @Query("submitcode") String submitcode, @Query("action") String action, @Header("Authorization") String authorization);

    /**
     * 根据腾讯云通信账号获取用户cust code
     *
     * @param code
     * @param submitcode
     * @param action
     * @return
     */
    @GET(Url.CUST)
    Observable<String> getCustCode(@Query("code") String code, @Query("submitcode") String submitcode, @Query("action") String action, @Header("Authorization") String authorization);

    /**
     * 获取用户信息
     *
     * @param submitCode
     * @param custCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.CUST)
    Observable<CustInfoRequestResponse> getCustInfo(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 获取用户详细信息
     *
     * @param actioncode
     * @param submitCode
     * @param custCode
     * @param selfCustCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.CUST)
    Observable<CustDetailInfoRequestResponse> getCustDetailInfo(@Query("actioncode") String actioncode, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("SelfCustCode") String selfCustCode, @Header("Authorization") String authorization);

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
    @POST(Url.SET_CUST)
    Observable<RequestResponse> setCustIMInfo(@Query("actioncode") String actioncode, @Field("SubmitCode") String submitCode, @Field("TCAccount") String tcAccount, @Field("Profile_Nick") String profile_Nick, @Field("Profile_Image") String profile_Image, @Field("Profile_AllowType") String profile_AllowType, @Field("Profile_SelfSignature") String profile_SelfSignature, @Header("Authorization") String authorization);

    /**
     * 设置用户详细信息
     *
     * @param submitCode
     * @param custCode
     * @param cName
     * @param cNickName
     * @param cSex
     * @param cBirthday
     * @param cTel
     * @param cIdNo
     * @param cNation
     * @param cHeight
     * @param cWeight
     * @param cProfession
     * @param cAddress
     * @param cProv
     * @param cCity
     * @param cDist
     * @param cCountry
     * @param cRemark
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SET_CUST)
    Observable<RequestResponse> setCustDetailInfo(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("CName") String cName, @Field("CNickName") String cNickName, @Field("CSex") String cSex, @Field("CBirthday") String cBirthday, @Field("CTel") String cTel, @Field("CIdNo") String cIdNo, @Field("CNation") String cNation, @Field("CHeight") Double cHeight, @Field("CWeight") Double cWeight, @Field("CProfession") String cProfession, @Field("CAddress") String cAddress, @Field("CProv") String cProv, @Field("CCity") String cCity, @Field("CDist") String cDist, @Field("CCountry") String cCountry, @Field("CRemark") String cRemark, @Header("Authorization") String authorization);

    /**
     * 普通用户升级为商家
     *
     * @param submitCode
     * @param custCode
     * @param bazaName
     * @param bazaPers
     * @param bazaTel
     * @param bazaAddr
     * @param remark
     * @param coor
     * @return
     */
    @FormUrlEncoded
    @POST(Url.REGISTER)
    Observable<RegisterBusinessRequestResponse> registerBusiness(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("BazaName") String bazaName, @Field("BazaPers") String bazaPers, @Field("BazaTel") String bazaTel, @Field("BazaAddr") String bazaAddr, @Field("Remark") String remark, @Field("Coor") String coor, @Header("Authorization") String authorization);

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
    Observable<FriendsRequestResponse> getFriendsList(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("TimeStampNow") Long timeStampNow, @Field("StartIndex") Integer startIndex, @Field("CurrentStandardSequence") Integer currentStandardSequence, @Field("PageSize") Integer pageSize, @Header("Authorization") String authorization);

    /**
     * 获取系统推荐好友
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param recomNum
     * @return
     */
    @FormUrlEncoded
    @POST(Url.FRIEND)
    Observable<RecommendFriendsRequestResponse> getRecommendFriends(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("RecomNum") Integer recomNum, @Header("Authorization") String authorization);

    /**
     * 获取金币
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param bazaCode
     * @param coor
     * @param ip
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASK)
    Observable<ScanCoinRequestResponse> scanQRCode(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("BazaCode") String bazaCode, @Field("Coor") String coor, @Field("Ip") String ip, @Header("Authorization") String authorization);

    /**
     * 游戏活动任务
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param gameCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GAME)
    Observable<GameTaskRequestResponse> getGameTasks(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("GameCode") String gameCode, @Header("Authorization") String authorization);

    /**
     * 上传用户头像
     *
     * @param action
     * @param fileName
     * @param fileData
     * @param tcAccount
     * @return
     */
    @FormUrlEncoded
    @POST(Url.FILE_UPLOAD)
    Observable<UploadFileRequestResponse> uploadPortrait(@Query("action") String action, @Field("FileName") String fileName, @Field("FileData") String fileData, @Field("TCAccount") String tcAccount, @Header("Authorization") String authorization);

    /**
     * 上传个人相册
     *
     * @param action
     * @param fileName
     * @param fileData
     * @param custCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.FILE_UPLOAD)
    Observable<UploadFileRequestResponse> uploadGalley(@Query("action") String action, @Field("FileName") String fileName, @Field("FileData") String fileData, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 获取个人的图片列表
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.IMAGE)
    Observable<GetGalleyRequestResponse> getPersonalGalley(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 删除个人相册
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param fileIdList
     * @return
     */
    @FormUrlEncoded
    @POST(Url.IMAGE)
    Observable<RequestResponse> deletePersonalGalley(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("FileIdList") List<Integer> fileIdList, @Header("Authorization") String authorization);

    /**
     * 定向活动签到数据列表
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param activityCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.ACTIVITY)
    Observable<SignDataRequestResponse> getSignActivityList(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("ActivityCode") String activityCode, @Header("Authorization") String authorization);

    /**
     * 获取周边商铺
     *
     * @param submitCode
     * @param coor
     * @param range
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOP_LIST)
    Observable<List<ShopListRequestResponse>> getShopList(@Field("SubmitCode") String submitCode, @Field("Coor") String coor, @Field("Range") Integer range, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Header("Authorization") String authorization);

    /**
     * 获取商铺详情
     *
     * @param submitCode
     * @param bazaCode
     * @param custCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOP_LIST)
    Observable<ShopInfoRequestResponse> getShopInfo(@Query("actioncode") String actioncode, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 设置对店铺关注
     *
     * @param submitCode
     * @param custCode
     * @param bazaCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SET_FOLLOW)
    Observable<RequestResponse> setFollow(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("BazaCode") String bazaCode, @Header("Authorization") String authorization);

    /**
     * 上传坐标
     *
     * @param submitCode
     * @param custCode
     * @param coor
     * @return
     */
    @FormUrlEncoded
    @POST(Url.BASE)
    Observable<RequestResponse> uploadCoor(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("Coor") String coor, @Header("Authorization") String authorization);

    /**
     * 获取当前用户周边用户
     *
     * @param submitCode
     * @param custCode
     * @param coor
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.PERSON)
    Observable<PersonRequestResponse> getPersonNearby(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("Coor") String coor, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Header("Authorization") String authorization);

    /**
     * 获取个人金币总数
     *
     * @param action
     * @param actiontype
     * @param submitCode
     * @param custCode
     * @param goldType
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD)
    Observable<ScanCoinRequestResponse> getPersonalLeftCoins(@Query("action") String action, @Query("actiontype") String actiontype, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("GoldType") Integer goldType, @Header("Authorization") String authorization);

    /**
     * 个人提现
     *
     * @param action
     * @param actiontype
     * @return
     */
    @POST(Url.GOLD)
    Observable<WithdrawRequestResponse> personalWithdrawCoins(@Query("action") String action, @Query("actiontype") String actiontype, @Body RequestBody personalWithdrawPara, @Header("Content-Type") String contentType, @Header("Accept") String accept, @Header("Authorization") String authorization);

    /**
     * 获取个人金币列表（背包功能）
     *
     * @param action
     * @param actiontype
     * @param submitCode
     * @param custCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD)
    Observable<BackgroundBagRequestResponse> personalBackgroundBag(@Query("action") String action, @Query("actiontype") String actiontype, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 个人提现日志
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence 保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<PersonalWithdrawLogRequestResponse> getPersonalWithdrawLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 获取用户关注的店铺信息
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.FOLLOW)
    Observable<FocusedShopRequestResponse> getFocusedShopList(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Header("Authorization") String authorization);

    /**
     * 用户获取金币日志列表
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<GetCoinsLogRequestResponse> getCoinsLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 获取个人金币类型
     *
     * @param submitCode
     * @param custCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD + "/GetTypelist")
    Observable<CoinTypeRequestResponse> getPersonalCoinTypes(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 个人去商铺消费支付
     *
     * @param action
     * @param paymentPara
     * @param contentType
     * @param accept
     * @param authorization
     * @return
     */
    @POST(Url.SHOP_PAY)
    Observable<PayRequestResponse> submitPayment(@Query("action") String action, @Body RequestBody paymentPara, @Header("Content-Type") String contentType, @Header("Accept") String accept, @Header("Authorization") String authorization);

    /**
     * 获取指定商铺兑换列表
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param custCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOP_EXCHANGE_LIST)
    Observable<ShopExchangeRequestResponse> getShopExchangeList(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 获取指定商铺兑换列表
     *
     * @param action
     * @param submitCode
     * @param goldType
     * @param custCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOP_EXCHANGE_LIST)
    Observable<ShopExchangeRequestResponse> getCoinExchangeList(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("GoldType") Integer goldType, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 兑换商品
     *
     * @param submitCode
     * @param exchangeId
     * @param custCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOP_EXCHANGE)
    Observable<RequestResponse> exchangeCoupon(@Field("SubmitCode") String submitCode, @Field("ExchangeId") String exchangeId, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 个人消费支付日志
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<PaymentLogRequestResponse> getPersonalPaymentLogs(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 个人兑换日志
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<CouponLogRequestResponse> getPersonalCouponLogs(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 商家获取商铺详情
     *
     * @param actioncode
     * @param submitCode
     * @param bazaCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOP_LIST)
    Observable<ShopInfoByPersRequestResponse> getShopInfoByPers(@Query("actioncode") String actioncode, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Header("Authorization") String authorization);

    /**
     * 设置商铺详细信息
     *
     * @param submitCode
     * @param bazaCode
     * @param bazaName
     * @param bazaPers
     * @param bazaTel
     * @param bazaAddr
     * @param remark
     * @param coor
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOP)
    Observable<RequestResponse> setShopInfos(@Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("BazaName") String bazaName, @Field("BazaPers") String bazaPers, @Field("BazaTel") String bazaTel, @Field("BazaAddr") String bazaAddr, @Field("Remark") String remark, @Field("Coor") String coor, @Header("Authorization") String authorization);

    /**
     * 获取指定店铺的获取金币的用户信息
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param custCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.PERSON)
    Observable<List<ShopScanner>> getShopFollowers(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Header("Authorization") String authorization);

    /**
     * 商户充值金币
     *
     * @param action
     * @return
     */
//    @FormUrlEncoded
    @POST(Url.GOLD)
//    Observable<RequestResponse> buyCoins(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("GoldNum") Integer goldNum, @Header("Authorization") String authorization);
    Observable<PayRequestResponse> buyCoins(@Query("action") String action, @Body RequestBody buyCoinPara, @Header("Content-Type") String contentType, @Header("Accept") String accept, @Header("Authorization") String authorization);

    /**
     * 购买金币付款
     *
     * @param payPara
     * @return
     */
//    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST(Url.PAY)
    Observable<PayRequestResponse> buyCoinsPay(@Body RequestBody payPara, @Header("Content-Type") String contentType, @Header("Accept") String accept, @Header("Authorization") String authorization);

    /**
     * 指定商铺购买金币日志列表
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<BuyCoinsLogRequestResponse> buyCoinsLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 获取商铺总金币数（不包括任务执行剩余金币，即可以使用或发布任务的金币总数）
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD)
    Observable<ScanCoinRequestResponse> getShopLeftCoins(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("GoldType") Integer goldType, @Header("Authorization") String authorization);

    /**
     * 获取商铺金币类型
     *
     * @param submitCode
     * @param bazaCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD + "/GetTypelist")
    Observable<CoinTypeRequestResponse> getShopCoinTypes(@Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Header("Authorization") String authorization);

    /**
     * 商铺提现
     *
     * @param action
     * @return
     */
    @POST(Url.GOLD)
    Observable<WithdrawRequestResponse> shopWithdrawCoins(@Query("action") String action, @Body RequestBody shopWithdrawPara, @Header("Content-Type") String contentType, @Header("Accept") String accept, @Header("Authorization") String authorization);

    /**
     * 商铺提现日志
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence 保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<ShopWithdrawLogRequestResponse> getShopWithdrawLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 商户创建金币任务
     *
     * @param submitCode
     * @param bazaCode
     * @param totalGold
     * @param totalNum
     * @param beginTime
     * @param endTime
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASK)
    Observable<RequestResponse> publishTasks(@Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("TotalGold") Integer totalGold, @Field("TotalNum") Integer totalNum, @Field("GoldType") Integer goldType, @Field("BeginTime") String beginTime, @Field("EndTime") String endTime, @Header("Authorization") String authorization);

    /**
     * 指定商铺获得发布任务日志列表
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<PublishTaskLogRequestResponse> publishTaskLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 上传商铺图片
     *
     * @param action
     * @param fileName
     * @param fileData
     * @param bazaCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.FILE_UPLOAD)
    Observable<UploadFileRequestResponse> uploadShopImage(@Query("action") String action, @Field("FileName") String fileName, @Field("FileData") String fileData, @Field("BazaCode") String bazaCode, @Header("Authorization") String authorization);

    /**
     * 获取关注店铺的用户信息
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.FOLLOW)
    Observable<ShopFocuserRequestResponse> getShopFocuser(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Header("Authorization") String authorization);

    /**
     * 获取指定商铺被扫码日志
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<ScannedLogRequestResponse> getScannedLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 获取商铺图片列表
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.IMAGE)
    Observable<GetGalleyRequestResponse> getShopGalley(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Header("Authorization") String authorization);

    /**
     * 删除商铺图片
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param fileIdList
     * @return
     */
    @FormUrlEncoded
    @POST(Url.IMAGE)
    Observable<RequestResponse> deleteShopGalley(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("FileIdList") List<Integer> fileIdList, @Header("Authorization") String authorization);

    /**
     * 获取指定商铺支付日志
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<ShopPayLogRequestResponse> getShopPayLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 获取支付单据
     *
     * @param submitCode
     * @param chargeId
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.PAY)
    Observable<PayRequestResponse> getNotPayCharge(@Field("SubmitCode") String submitCode, @Field("ChargeId") String chargeId, @Header("Authorization") String authorization);

    /**
     * 获取商铺金币列表（背包功能）
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD)
    Observable<BackgroundBagRequestResponse> shopBackgroundBag(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Header("Authorization") String authorization);

    /**
     * 获取商铺打折列表
     *
     * @param submitCode
     * @param bazaCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOP_DISCOUNT)
    Observable<ShopDiscountRequestResponse> getShopDiscounts(@Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Header("Authorization") String authorization);

    /**
     * 商铺消费支付日志
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<PaymentLogRequestResponse> getShopPaymentLogs(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 商铺兑换商品日志
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<CouponLogRequestResponse> getShopCouponLogs(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 获取商铺发布任务剩余总金币数
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD)
    Observable<ShopTaskLeftCoinsRequestResponse> getShopTaskLeftCoins(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Header("Authorization") String authorization);

    /**
     * 获取推荐列表
     *
     * @param submitCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.EXPAND)
    Observable<List<ExpandListRequestResponse>> getExpandList(@Field("SubmitCode") String submitCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Header("Authorization") String authorization);

    /**
     * 获取推荐轮播
     *
     * @param submitCode
     * @param actionCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.DISCOVER)
    Observable<RecommendBannerRequestResponse> getRecommendBanner(@Field("SubmitCode") String submitCode, @Field("ActionCode") String actionCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 获取推荐新闻列表
     *
     * @param submitCode
     * @param actionCode
     * @param pageIndex
     * @param pageSize
     * @param lastsequence
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.DISCOVER)
    Observable<RecommendNewsRequestResponse> getRecommendNews(@Field("SubmitCode") String submitCode, @Field("ActionCode") String actionCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Field("lastsequence") Integer lastsequence, @Header("Authorization") String authorization);

    /**
     * 获取综合体列表
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.ACTIVITY)
    Observable<MarketsInfoRequestResponse> getMarketsInfo(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 综合体商铺列表
     *
     * @param submitCode
     * @param complexKeyword
     * @param storeyKeyword
     * @param pageIndex
     * @param pageSize
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.ACTIVITY)
    Observable<List<MarketShop>> getMarketShops(@Field("SubmitCode") String submitCode, @Field("ComplexKeyword") String complexKeyword, @Field("StoreyKeyword") String storeyKeyword, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize, @Header("Authorization") String authorization);

    /**
     * 校验银行卡
     *
     * @param submitCode
     * @param bankCardNum
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.CHECK_CARD)
    Observable<ValidateBankCardRequestResponse> validateBankCard(@Field("SubmitCode") String submitCode, @Field("BankCardNum") String bankCardNum, @Header("Authorization") String authorization);

    /**
     * 获取绑定银行卡数据
     *
     * @param submitCode
     * @param custCode
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.CUST + "/GetBankList")
    Observable<List<BankCard>> getBankCardList(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Header("Authorization") String authorization);

    /**
     * 绑定银行卡
     *
     * @param submitCode
     * @param custCode
     * @param bankCode
     * @param bankCardNum
     * @param custName
     * @param authorization
     * @return
     */
    @FormUrlEncoded
    @POST(Url.CUST + "/BindBank")
    Observable<RequestResponse> bindBankCard(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("BankCode") String bankCode, @Field("BankCardNum") String bankCardNum, @Field("CustName") String custName, @Header("Authorization") String authorization);
}
