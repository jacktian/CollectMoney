package com.yzdsmart.Dingdingwen.main;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupPendencyGetParam;
import com.tencent.TIMGroupPendencyListGetSucc;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMUserStatusListener;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.MarketShop;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.BackgroundBagRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetTokenRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.MarketsInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopListRequestResponse;
import com.yzdsmart.Dingdingwen.tecent_im.bean.FriendshipInfo;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupInfo;
import com.yzdsmart.Dingdingwen.tecent_im.bean.UserInfo;
import com.yzdsmart.Dingdingwen.tecent_im.business.InitBusiness;
import com.yzdsmart.Dingdingwen.tecent_im.business.LoginBusiness;
import com.yzdsmart.Dingdingwen.tecent_im.event.FriendshipEvent;
import com.yzdsmart.Dingdingwen.tecent_im.event.GroupEvent;
import com.yzdsmart.Dingdingwen.tecent_im.event.MessageEvent;
import com.yzdsmart.Dingdingwen.tecent_im.event.RefreshEvent;
import com.yzdsmart.Dingdingwen.tecent_im.service.TLSService;
import com.yzdsmart.Dingdingwen.tecent_im.service.TlsBusiness;
import com.yzdsmart.Dingdingwen.tecent_im.utils.PushUtil;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by YZD on 2016/8/30.
 */
public class MainPresenter implements MainContract.MainPresenter, Observer, TIMCallBack {
    private Context context;
    private MainContract.MainView mView;
    private MainModel mModel;
    private DecimalFormat decimalFormat;

    public MainPresenter(Context context, MainContract.MainView mView, TLSService tlsService) {
        this.context = context;
        this.mView = mView;
        mModel = new MainModel(tlsService);
        mView.setPresenter(this);
        decimalFormat = new DecimalFormat("#0");

        //初始化IMSDK
        InitBusiness.start(App.getAppInstance(), TIMLogLevel.DEBUG.ordinal());
        //初始化TLS
        TlsBusiness.init(App.getAppInstance());
    }

    @Override
    public void chatLogin(String im_name, String im_pwd) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loginning));
        mModel.chatLogin(im_name, im_pwd, new TLSPwdLoginListener() {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
                ((BaseActivity) context).hideProgressDialog();
                TLSService.getInstance().setLastErrno(0);
                String id = TLSService.getInstance().getLastUserIdentifier();
                UserInfo.getInstance().setId(id);
                UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
                mView.chatLoginSuccess();
            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {
                ((BaseActivity) context).hideProgressDialog();
            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
                ((BaseActivity) context).hideProgressDialog();
            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
                ((BaseActivity) context).hideProgressDialog();
                TLSService.getInstance().setLastErrno(-1);
                ((BaseActivity) context).showSnackbar(String.format("%s: %s",
                        tlsErrInfo.ErrCode == TLSErrInfo.TIMEOUT ?
                                "网络超时" : "错误", tlsErrInfo.Msg));
                logoutError();
            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
                ((BaseActivity) context).hideProgressDialog();
                TLSService.getInstance().setLastErrno(-1);
                ((BaseActivity) context).showSnackbar(String.format("%s: %s",
                        tlsErrInfo.ErrCode == TLSErrInfo.TIMEOUT ?
                                "网络超时" : "错误", tlsErrInfo.Msg));
                logoutError();
            }
        });
    }

    @Override
    public void imSDKLogin() {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loginning));
        //登录之前要初始化群和好友关系链缓存
        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), this);
    }

    @Override
    public void getConversation() {
        List<TIMConversation> list = new ArrayList<TIMConversation>();
        //获取会话个数
        long cnt = TIMManager.getInstance().getConversationCount();
        //遍历会话列表
        for (long i = 0; i < cnt; ++i) {
            //根据索引获取会话
            final TIMConversation conversation = TIMManager.getInstance().getConversationByIndex(i);
            if (conversation.getType() == TIMConversationType.System) continue;
            list.add(conversation);
            conversation.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    ((BaseActivity) context).showSnackbar("获取消息失败:" + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        mView.updateConversation(timMessages.get(0));
                    }
                }
            });
        }
        mView.initConversations(list);
    }

    @Override
    public void getGroupManageLastMessage() {
        TIMGroupPendencyGetParam param = new TIMGroupPendencyGetParam();
        param.setNumPerPage(1);
        param.setTimestamp(0);
        TIMGroupManager.getInstance().getGroupPendencyList(param, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int i, String s) {
                System.out.println("onError code" + i + " msg " + s);
            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                if (mView != null && timGroupPendencyListGetSucc.getPendencies().size() > 0) {
                    mView.onGetGroupManageLastMessage(timGroupPendencyListGetSucc.getPendencies().get(0),
                            timGroupPendencyListGetSucc.getPendencyMeta().getUnReadCount());
                }
            }
        });
    }

    @Override
    public void unRegisterObserver() {
        //解除消息监听
        MessageEvent.getInstance().deleteObserver(this);
        //解除刷新监听
        RefreshEvent.getInstance().deleteObserver(this);
        //解除好友关系链监听
        FriendshipEvent.getInstance().deleteObserver(this);
        //解除群关系监听
        GroupEvent.getInstance().deleteObserver(this);
        PushUtil.getInstance(context).release();

        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
    }

    @Override
    public void appRegister(String appCode, String appId, String appSecret) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.init_app));
        mModel.appRegister(appCode, appId, appSecret, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onAppRegister(true);
                } else {
                    mView.onAppRegister(false);
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                mView.onAppRegister(false);
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void getRefreshToken(String grantType, String userName, String password) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.init_app));
        mModel.getRefreshToken(grantType, userName, password, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetTokenRequestResponse requestResponse = (GetTokenRequestResponse) result;
                if (null != requestResponse) {
                    SharedPreferencesUtils.setString(context, "ddw_refresh_token", requestResponse.getRefresh_token());
                    SharedPreferencesUtils.setString(context, "ddw_access_token", requestResponse.getAccess_token());
                    SharedPreferencesUtils.setString(context, "ddw_token_type", requestResponse.getToken_type());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(err);
                if (err.contains("400 Bad Request")) {
                    mView.reRegisterApp();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void refreshAccessToken(String grantType, String refreshToken) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.init_app));
        mModel.refreshAccessToken(grantType, refreshToken, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetTokenRequestResponse requestResponse = (GetTokenRequestResponse) result;
                if (null != requestResponse) {
                    SharedPreferencesUtils.setString(context, "ddw_refresh_token", requestResponse.getRefresh_token());
                    SharedPreferencesUtils.setString(context, "ddw_access_token", requestResponse.getAccess_token());
                    SharedPreferencesUtils.setString(context, "ddw_token_type", requestResponse.getToken_type());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                if (err.contains("400 Bad Request")) {
                    mView.getRefreshToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void personalBackgroundBag(String action, String actiontype, String submitCode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.personalBackgroundBag(action, actiontype, submitCode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                BackgroundBagRequestResponse requestResponse = (BackgroundBagRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetBackgroundBag(requestResponse.getLists());
                } else {
                    mView.onDismissBackgroundBag();
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                mView.onDismissBackgroundBag();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_background_bag));
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void shopBackgroundBag(String action, String submitCode, String bazaCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.shopBackgroundBag(action, submitCode, bazaCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                BackgroundBagRequestResponse requestResponse = (BackgroundBagRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetBackgroundBag(requestResponse.getLists());
                } else {
                    mView.onDismissBackgroundBag();
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                mView.onDismissBackgroundBag();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_background_bag));
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void getShopList(String submitCode, String coor, Integer range, Integer pageIndex, Integer pageSize, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopList(submitCode, coor, range, pageIndex, pageSize, authorization, new RequestListener() {
                    @Override
                    public void onSuccess(Object result) {
                        List<ShopListRequestResponse> shopList = (List<ShopListRequestResponse>) result;
                        if (null != shopList) {
                            // 构建MarkerOption，用于在地图上添加Marker
                            MarkerOptions options;
                            List<MarkerOptions> optionsList = new ArrayList<MarkerOptions>();
                            for (ShopListRequestResponse shop : shopList) {
                                String coor = shop.getCoor();
                                // 定义Maker坐标点
                                LatLng point = new LatLng(Double.valueOf(coor.split(",")[1]), Double.valueOf(coor.split(",")[0]));
                                View packageView = ((BaseActivity) context).getLayoutInflater().inflate(R.layout.red_package_icon, null);
                                TextView amountTV = (TextView) packageView.findViewById(R.id.package_amount);
                                amountTV.setText(decimalFormat.format(shop.getReleGold()));
                                options = new MarkerOptions().position(point).snippet(shop.getCode()).icon(BitmapDescriptorFactory.fromView(packageView));
                                optionsList.add(options);
                            }
                            mView.onGetShopList(optionsList);
                        }
                    }

                    @Override
                    public void onError(String err) {
                        ((BaseActivity) context).hideProgressDialog();
                        if (err.contains("401 Unauthorized")) {
                            MainActivity.getInstance().updateAccessToken();
                        }
                    }

                    @Override
                    public void onComplete() {
                        ((BaseActivity) context).hideProgressDialog();
                    }
                }
        );
    }

    @Override
    public void uploadCoor(String submitCode, String custCode, String coor, String authorization) {
        mModel.uploadCoor(submitCode, custCode, coor, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(String err) {
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getMarketsInfo(String action, String submitCode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getMarketsInfo(action, submitCode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                MarketsInfoRequestResponse requestResponse = (MarketsInfoRequestResponse) result;
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void getMarketShops(String submitCode, String complexKeyword, String storeyKeyword, Integer pageIndex, Integer pageSize, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getMarketShops(submitCode, complexKeyword, storeyKeyword, pageIndex, pageSize, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<MarketShop> marketShops = (List<MarketShop>) result;
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            TIMMessage msg = (TIMMessage) data;
            mView.updateConversation(msg);
        } else if (observable instanceof FriendshipEvent) {
            FriendshipEvent.NotifyCmd fcmd = (FriendshipEvent.NotifyCmd) data;
            switch (fcmd.type) {
                case ADD_REQ:
                    break;
                case ADD:
                case REFRESH:
                case DEL:
                    break;
            }
        } else if (observable instanceof GroupEvent) {
            GroupEvent.NotifyCmd gcmd = (GroupEvent.NotifyCmd) data;
            switch (gcmd.type) {
                case UPDATE:
                case ADD:
                    break;
                case DEL:
                    break;
            }
        } else if (observable instanceof RefreshEvent) {

        }
    }

    @Override
    public void onError(int i, String s) {
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                logoutError();
                break;
            default:
                logoutError();
                break;
        }
    }

    @Override
    public void onSuccess() {
        ((BaseActivity) context).hideProgressDialog();
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        //注册刷新监听
        RefreshEvent.getInstance().addObserver(this);
        //注册好友关系链监听
        FriendshipEvent.getInstance().addObserver(this);
        //注册群关系监听
        GroupEvent.getInstance().addObserver(this);
        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                //别的设备登录,强行退出
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.logout_warning));
                logoutError();
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.user_sig_warning));
                logoutError();
            }
        });
        mView.imSDKLoginSuccess();
    }

    private void logoutError() {
        ((BaseActivity) context).hideProgressDialog();
        unRegisterObserver();
        String platformName = SharedPreferencesUtils.getString(context, "platform", "");
        if (null != platformName && platformName.length() > 0) {
            Platform platform = null;
            if ("qq".equals(platformName)) {
                platform = ShareSDK.getPlatform(context, QQ.NAME);
            } else if ("wb".equals(platformName)) {
                platform = ShareSDK.getPlatform(context, SinaWeibo.NAME);
            } else if ("wx".equals(platformName)) {
                platform = ShareSDK.getPlatform(context, Wechat.NAME);
            }
            if (platform.isAuthValid()) {
                platform.removeAccount(true);
            }
            SharedPreferencesUtils.remove(context, "platform");
            SharedPreferencesUtils.remove(context, "exportData");
            SharedPreferencesUtils.remove(context, "userGender");
            SharedPreferencesUtils.remove(context, "userNickName");
        }
        SharedPreferencesUtils.remove(context, "baza_code");
        SharedPreferencesUtils.remove(context, "cust_code");
        SharedPreferencesUtils.remove(context, "im_account");
        SharedPreferencesUtils.remove(context, "im_password");
        mView.onIMOffline();
    }
}
