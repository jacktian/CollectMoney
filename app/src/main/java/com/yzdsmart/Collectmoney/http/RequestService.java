package com.yzdsmart.Collectmoney.http;

import com.yzdsmart.Collectmoney.bean.ShopScanner;
import com.yzdsmart.Collectmoney.http.response.BuyCoinLogRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustLevelRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ExpandListRequestResponse;
import com.yzdsmart.Collectmoney.http.response.FocusedShopRequestResponse;
import com.yzdsmart.Collectmoney.http.response.FriendsRequestResponse;
import com.yzdsmart.Collectmoney.http.response.GetCoinRequestResponse;
import com.yzdsmart.Collectmoney.http.response.GetCoinsLogRequestResponse;
import com.yzdsmart.Collectmoney.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Collectmoney.http.response.LoginRequestResponse;
import com.yzdsmart.Collectmoney.http.response.PersonRequestResponse;
import com.yzdsmart.Collectmoney.http.response.PersonalWithdrawLogRequestResponse;
import com.yzdsmart.Collectmoney.http.response.PublishTaskLogRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RegisterBusinessRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopFocuserRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopListRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopWithdrawLogRequestResponse;
import com.yzdsmart.Collectmoney.http.response.UploadFileRequestResponse;
import com.yzdsmart.Collectmoney.http.response.WithdrawRequestResponse;

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
    @POST(Url.SMS)
    Observable<RequestResponse> validateVerifyCode(@Query("actioncode") String actioncode, @Field("Tel") String telNum, @Field("Sms_Veri_Code") String verifyCode);

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
    Observable<RequestResponse> setPassword(@Query("actioncode") String actioncode, @Field("UserName") String userName, @Field("Password") String password, @Field("RegCode") String regCode);

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
    Observable<RequestResponse> userRegister(@Query("actioncode") String actioncode, @Field("UserName") String userName, @Field("Password") String password, @Field("CSex") String cSex, @Field("CAge") Integer cAge, @Field("CNickName") String cNickName, @Field("RegCode") String regCode);

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
     * 获取用户等级和星级
     *
     * @param custcode
     * @param submitcode
     * @return
     */
    @GET(Url.CUST)
    Observable<CustLevelRequestResponse> getCustLevel(@Query("custcode") String custcode, @Query("submitcode") String submitcode);

    /**
     * 获取用户信息
     *
     * @param submitCode
     * @param custCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.CUST)
    Observable<CustInfoRequestResponse> getCustInfo(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode);

    /**
     * 获取用户详细信息
     *
     * @param actioncode
     * @param submitCode
     * @param custCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.CUST)
    Observable<CustDetailInfoRequestResponse> getCustDetailInfo(@Query("actioncode") String actioncode, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode);

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
    Observable<RequestResponse> setCustIMInfo(@Query("actioncode") String actioncode, @Field("SubmitCode") String submitCode, @Field("TCAccount") String tcAccount, @Field("Profile_Nick") String profile_Nick, @Field("Profile_Image") String profile_Image, @Field("Profile_AllowType") String profile_AllowType, @Field("Profile_SelfSignature") String profile_SelfSignature);

    /**
     * 设置用户详细信息
     *
     * @param submitCode
     * @param custCode
     * @param cName
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
    Observable<RequestResponse> setCustDetailInfo(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("CName") String cName, @Field("CSex") String cSex, @Field("CBirthday") String cBirthday, @Field("CTel") String cTel, @Field("CIdNo") String cIdNo, @Field("CNation") String cNation, @Field("CHeight") Double cHeight, @Field("CWeight") Double cWeight, @Field("CProfession") String cProfession, @Field("CAddress") String cAddress, @Field("CProv") String cProv, @Field("CCity") String cCity, @Field("CDist") String cDist, @Field("CCountry") String cCountry, @Field("CRemark") String cRemark);

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
    Observable<RegisterBusinessRequestResponse> registerBusiness(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("BazaName") String bazaName, @Field("BazaPers") String bazaPers, @Field("BazaTel") String bazaTel, @Field("BazaAddr") String bazaAddr, @Field("Remark") String remark, @Field("Coor") String coor);

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
     * 获取商铺关注者
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
    Observable<GetCoinRequestResponse> getGoldCoins(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("BazaCode") String bazaCode, @Field("Coor") String coor, @Field("Ip") String ip);

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
    Observable<UploadFileRequestResponse> uploadPortrait(@Query("action") String action, @Field("FileName") String fileName, @Field("FileData") String fileData, @Field("TCAccount") String tcAccount);

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
    Observable<UploadFileRequestResponse> uploadGalley(@Query("action") String action, @Field("FileName") String fileName, @Field("FileData") String fileData, @Field("CustCode") String custCode);

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
    Observable<GetGalleyRequestResponse> getPersonalGalley(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode);

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
    Observable<RequestResponse> deletePersonalGalley(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("FileIdList") List<Integer> fileIdList);

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
    Observable<List<ShopListRequestResponse>> getShopList(@Field("SubmitCode") String submitCode, @Field("Coor") String coor, @Field("Range") Integer range, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

    /**
     * 获取商铺详情
     *
     * @param submitCode
     * @param bazaCode
     * @param custCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SHOP_LIST)
    Observable<ShopInfoRequestResponse> getShopInfo(@Query("actioncode") String actioncode, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("CustCode") String custCode);

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
    Observable<RequestResponse> setFollow(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("BazaCode") String bazaCode);

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
    Observable<List<ExpandListRequestResponse>> getExpandList(@Field("SubmitCode") String submitCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

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
    Observable<RequestResponse> uploadCoor(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("Coor") String coor);

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
    Observable<PersonRequestResponse> getPersonNearby(@Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("Coor") String coor, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

    /**
     * 个人提现
     *
     * @param action
     * @param actiontype
     * @param submitCode
     * @param custCode
     * @param goldNum
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD)
    Observable<WithdrawRequestResponse> personalWithdrawCoins(@Query("action") String action, @Query("actiontype") String actiontype, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("GoldNum") Integer goldNum);

    /**
     * 个人提现日志
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<PersonalWithdrawLogRequestResponse> getPersonalWithdrawLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

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
    Observable<FocusedShopRequestResponse> getFocusedShopList(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

    /**
     * 用户获取金币日志列表
     *
     * @param action
     * @param submitCode
     * @param custCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<GetCoinsLogRequestResponse> getCoinsLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

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
    Observable<List<ShopScanner>> getShopFollowers(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("CustCode") String custCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

    /**
     * 商户充值金币
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param goldNum
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD)
    Observable<RequestResponse> buyCoins(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("GoldNum") Integer goldNum);

    /**
     * 指定商铺购买金币日志列表
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<BuyCoinLogRequestResponse> buyCoinsLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

    /**
     * 获取店铺剩余金币数
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD)
    Observable<GetCoinRequestResponse> getLeftCoins(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode);

    /**
     * 商铺提现
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param goldNum
     * @return
     */
    @FormUrlEncoded
    @POST(Url.GOLD)
    Observable<WithdrawRequestResponse> shopWithdrawCoins(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("GoldNum") Integer goldNum);

    /**
     * 商铺提现日志
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<ShopWithdrawLogRequestResponse> getShopWithdrawLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);

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
    Observable<RequestResponse> publishTasks(@Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("TotalGold") Integer totalGold, @Field("TotalNum") Integer totalNum, @Field("BeginTime") String beginTime, @Field("EndTime") String endTime);


    /**
     * 指定商铺获得发布任务日志列表
     *
     * @param action
     * @param submitCode
     * @param bazaCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(Url.TASKLOG)
    Observable<PublishTaskLogRequestResponse> publishTaskLog(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);


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
    Observable<UploadFileRequestResponse> uploadShopImage(@Query("action") String action, @Field("FileName") String fileName, @Field("FileData") String fileData, @Field("BazaCode") String bazaCode);

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
    Observable<ShopFocuserRequestResponse> getShopFocuser(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("PageIndex") Integer pageIndex, @Field("PageSize") Integer pageSize);


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
    Observable<GetGalleyRequestResponse> getShopGalley(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode);

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
    Observable<RequestResponse> deleteShopGalley(@Query("action") String action, @Field("SubmitCode") String submitCode, @Field("BazaCode") String bazaCode, @Field("FileIdList") List<Integer> fileIdList);
}
