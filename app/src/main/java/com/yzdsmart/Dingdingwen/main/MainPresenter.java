package com.yzdsmart.Dingdingwen.main;

import android.content.Context;
import android.preference.PreferenceManager;

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
import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.bean.FriendshipInfo;
import com.yzdsmart.Collectmoney.tecent_im.bean.GroupInfo;
import com.yzdsmart.Collectmoney.tecent_im.bean.UserInfo;
import com.yzdsmart.Collectmoney.tecent_im.business.InitBusiness;
import com.yzdsmart.Collectmoney.tecent_im.business.LoginBusiness;
import com.yzdsmart.Collectmoney.tecent_im.event.FriendshipEvent;
import com.yzdsmart.Collectmoney.tecent_im.event.GroupEvent;
import com.yzdsmart.Collectmoney.tecent_im.event.MessageEvent;
import com.yzdsmart.Collectmoney.tecent_im.event.RefreshEvent;
import com.yzdsmart.Collectmoney.tecent_im.service.TLSService;
import com.yzdsmart.Collectmoney.tecent_im.service.TlsBusiness;
import com.yzdsmart.Collectmoney.tecent_im.utils.PushUtil;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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

    public MainPresenter(Context context, MainContract.MainView mView, TLSService tlsService) {
        this.context = context;
        this.mView = mView;
        mModel = new MainModel(tlsService);
        mView.setPresenter(this);

        //初始化IMSDK
        InitBusiness.start(App.getAppInstance(), TIMLogLevel.DEBUG.ordinal());
        //初始化TLS
        TlsBusiness.init(App.getAppInstance());
    }

    @Override
    public void chatLogin(String im_name, String im_pwd) {
        mModel.chatLogin(im_name, im_pwd, new TLSPwdLoginListener() {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
                TLSService.getInstance().setLastErrno(0);
                String id = TLSService.getInstance().getLastUserIdentifier();
                UserInfo.getInstance().setId(id);
                UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
                mView.chatLoginSuccess();
            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {

            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {

            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
                TLSService.getInstance().setLastErrno(-1);
                ((BaseActivity) context).showSnackbar(String.format("%s: %s",
                        tlsErrInfo.ErrCode == TLSErrInfo.TIMEOUT ?
                                "网络超时" : "错误", tlsErrInfo.Msg));
            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
                TLSService.getInstance().setLastErrno(-1);
                ((BaseActivity) context).showSnackbar(String.format("%s: %s",
                        tlsErrInfo.ErrCode == TLSErrInfo.TIMEOUT ?
                                "网络超时" : "错误", tlsErrInfo.Msg));
            }
        });
    }

    @Override
    public void imSDKLogin() {
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
        PushUtil.getInstance().release();

        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
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
                System.out.println("--------------------------------" + context.getResources().getString(R.string.kick_logout));
                unRegisterObserver();
                SharedPreferencesUtils.clear(context, PreferenceManager.getDefaultSharedPreferences(context));
                mView.onIMOffline();
                break;
            default:
                System.out.println("--------------------------------" + context.getResources().getString(R.string.login_error));
                unRegisterObserver();
                SharedPreferencesUtils.clear(context, PreferenceManager.getDefaultSharedPreferences(context));
                mView.onIMOffline();
                break;
        }
    }

    @Override
    public void onSuccess() {
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
                unRegisterObserver();
                SharedPreferencesUtils.clear(context, PreferenceManager.getDefaultSharedPreferences(context));
                mView.onIMOffline();
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.user_sig_warning));
                unRegisterObserver();
                SharedPreferencesUtils.clear(context, PreferenceManager.getDefaultSharedPreferences(context));
                mView.onIMOffline();
            }
        });
        mView.imSDKLoginSuccess();
    }
}
