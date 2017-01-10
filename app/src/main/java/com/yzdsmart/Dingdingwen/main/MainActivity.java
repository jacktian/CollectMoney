package com.yzdsmart.Dingdingwen.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupPendencyItem;
import com.tencent.TIMMessage;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.CoinType;
import com.yzdsmart.Dingdingwen.coupon_exchange.CouponExchangeActivity;
import com.yzdsmart.Dingdingwen.main.find_money.FindMoneyFragment;
import com.yzdsmart.Dingdingwen.main.recommend.RecommendFragment;
import com.yzdsmart.Dingdingwen.money_friendship.MoneyFriendshipActivity;
import com.yzdsmart.Dingdingwen.money_friendship.conversation.ConversationFragment;
import com.yzdsmart.Dingdingwen.personal.PersonalActivity;
import com.yzdsmart.Dingdingwen.register_login.login.LoginActivity;
import com.yzdsmart.Dingdingwen.service.RefreshAccessTokenService;
import com.yzdsmart.Dingdingwen.tecent_im.bean.Conversation;
import com.yzdsmart.Dingdingwen.tecent_im.bean.CustomMessage;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupInfo;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupManageConversation;
import com.yzdsmart.Dingdingwen.tecent_im.bean.MessageFactory;
import com.yzdsmart.Dingdingwen.tecent_im.bean.NormalConversation;
import com.yzdsmart.Dingdingwen.tecent_im.bean.UserInfo;
import com.yzdsmart.Dingdingwen.tecent_im.service.TLSService;
import com.yzdsmart.Dingdingwen.tecent_im.utils.PushUtil;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.GuideView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by YZD on 2016/8/17.
 */
public class MainActivity extends BaseActivity implements MainContract.MainView {
    //    , EasyPermissions.PermissionCallbacks
    @Nullable
    @BindView(R.id.unread_conversation_bubble)
    TextView unreadConversationBubbleTV;
    @Nullable
    @BindView(R.id.loc_scan_coins)
    ImageButton searchIB;

    private UltimateRecyclerView backgroundBagRV;
    private TextView backgroundBagLoginCheck;

    //连续双击返回键退出程序
    private Long lastKeyDown = 0l;

    private FragmentManager fm;
    private Fragment mCurrentFragment;
    private String mCurrentTag = "";

    private MainContract.MainPresenter mPresenter;

    private TLSService tlsService;

    private static MainActivity mainActivity;

    public static MainActivity getInstance() {
        return mainActivity;
    }

    private List<Conversation> conversationList;
    private List<Conversation> pgConversationList = null;//个人或者群消息
    private GroupManageConversation groupManageConversation;

    private Handler mRefreshAccessTokenHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!Utils.isNetUsable(MainActivity.this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            if (SharedPreferencesUtils.getString(MainActivity.this, "ddw_refresh_token", "") != null && SharedPreferencesUtils.getString(MainActivity.this, "ddw_refresh_token", "").trim().length() > 0) {
                mPresenter.refreshAccessToken("refresh_token", SharedPreferencesUtils.getString(MainActivity.this, "ddw_refresh_token", ""));
            } else {
                getRefreshToken();
            }
        }
    };
    private boolean stopRefreshAccessToken = false;//停止刷新

    private ServiceConnection mConnection;
    private RefreshAccessTokenService mService;

    private GridLayoutManager mGridLayoutManager;
    private BackgroundBagAdapter bagAdapter;
    private boolean isFirstLoadBag = true;
    private BottomSheetDialog backgroundBagBottomSheetDialog;

    private List<String> customMsgList;
    private Handler showCustomMsgHandler = new Handler();
    private Runnable showCustomMsgRunnable;
    private Timer customMsgTimer;
    private TimerTask customMsgTask;

    private GuideView searchGuideView;

    public void showSearchGuide() {
        ImageView searchIV = new ImageView(this);
        searchIV.setImageResource(R.mipmap.search_guide_view);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchIV.setLayoutParams(params);

        searchGuideView = GuideView.Builder
                .newInstance(this)
                .setTargetView(searchIB)//设置目标
                .setCustomGuideView(searchIV)
                .setDirction(GuideView.Direction.TOP)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        SharedPreferencesUtils.setString(MainActivity.this, "hasGuide", "true");
                        searchGuideView.hide();
                    }
                })
                .build();

        searchGuideView.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = this;

        conversationList = new LinkedList<Conversation>();
        pgConversationList = new ArrayList<Conversation>();
        customMsgList = new ArrayList<String>();

        fm = getFragmentManager();

        if (null != savedInstanceState) {
            mCurrentFragment = getFragmentManager().getFragment(savedInstanceState, "currentFragment");
            mCurrentTag = savedInstanceState.getString("currentTag");
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.layout_frame, mCurrentFragment, mCurrentTag);
            ft.commitAllowingStateLoss();
        } else {
            initView();
        }

        bagAdapter = new BackgroundBagAdapter(this);
        mGridLayoutManager = new GridLayoutManager(this, 5);

        initBackgroundBagBottomSheetDialog();

//        getLocationPermission();//获取手机定位权限

        tlsService = TLSService.getInstance();

        new MainPresenter(this, this, tlsService);

        registerCustomMessageReceiver();

        imLogin();

        initJPush();

        showCustomMsgRunnable = new Runnable() {
            @Override
            public void run() {
                if (null == customMsgList || customMsgList.size() <= 0) {
//                    showCustomMsgHandler.removeCallbacks(this);
                    return;
                }
                showCustomMsg(customMsgList.get(0));
                customMsgList.remove(0);
            }
        };
        customMsgTimer = new Timer();
        customMsgTask = new TimerTask() {
            @Override
            public void run() {
                showCustomMsgHandler.post(showCustomMsgRunnable);
            }
        };
        customMsgTimer.schedule(customMsgTask, 1000, 3000);

//        mConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                RefreshAccessTokenService.RefreshAccessTokenBinder binder = (RefreshAccessTokenService.RefreshAccessTokenBinder) iBinder;
//                mService = binder.getService();
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//                mService = null;
//            }
//        };

        getRefreshToken();
        refreshAccessToken();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    private void initView() {
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = new FindMoneyFragment();
        mCurrentTag = "find";
        ft.add(R.id.layout_frame, mCurrentFragment, mCurrentTag);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != UserInfo.getInstance().getId()) {
            PushUtil.getInstance(this).reset();
            updateUnreadConversationBubble();
        }
        MobclickAgent.onResume(this);       //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unRegisterObserver();
        JPushInterface.stopPush(App.getAppInstance());
        stopRefreshAccessToken = true;
        if (null != showCustomMsgHandler && null != showCustomMsgRunnable) {
            showCustomMsgHandler.removeCallbacks(showCustomMsgRunnable);
        }
        if (null != customMsgTimer) {
            customMsgTimer.cancel();
            customMsgTimer = null;
        }
        if (null != mService) {
            unbindService(mConnection);
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            imLogin();
        } else if (Constants.REQUEST_LOGIN_UPDATE_BAGCKGROUND_BAG_CODE == requestCode && RESULT_OK == resultCode) {
            ButterKnife.apply(backgroundBagLoginCheck, BUTTERKNIFEGONE);
            ButterKnife.apply(backgroundBagRV, BUTTERKNIFEVISIBLE);
            imLogin();
            bagAdapter.clearList();
            getBackgroundBag();
        }
    }

    @Optional
    @OnClick({R.id.money_friend_radio, R.id.loc_scan_coins, R.id.personal_radio, R.id.background_bag_layout, R.id.quit_background_bag})
    void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.money_friend_radio:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                    return;
                }
                openActivity(MoneyFriendshipActivity.class);
                if (!(mCurrentFragment instanceof FindMoneyFragment)) {
                    backToFindMoney();
                }
                break;
            case R.id.loc_scan_coins:
                if (!(mCurrentFragment instanceof FindMoneyFragment)) {
                    backToFindMoney();
                }
                fragment = fm.findFragmentByTag("find");
                if (null != fragment) {
                    ((FindMoneyFragment) fragment).locScanCoins();
                }
                break;
            case R.id.personal_radio:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                    return;
                }
                openActivity(PersonalActivity.class);
                if (!(mCurrentFragment instanceof FindMoneyFragment)) {
                    backToFindMoney();
                }
                break;
        }
    }

    /**
     * 返回到找钱页
     */
    public void backToFindMoney() {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null == fragment) {
            fragment = new FindMoneyFragment();
        }
        addOrShowFragment(fragment, "find");
    }

    /**
     * 添加或者显示碎片
     *
     * @param fragment
     * @param tag
     */
    public void addOrShowFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        if (!fragment.isAdded()) {
            ft.hide(mCurrentFragment).add(R.id.layout_frame, fragment, tag);
        } else {
            ft.hide(mCurrentFragment).show(fragment);
        }
        ft.commitAllowingStateLoss();
        mCurrentFragment = fragment;
        mCurrentTag = tag;
    }

    public void getShopListNearByMarket(String name, String location) {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null != fragment) {
            ((FindMoneyFragment) fragment).getShopListNearByMarket(name, location);
        }
    }

    public void planRoute(String coor, String shopName) {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null != fragment) {
            ((FindMoneyFragment) fragment).planRoute(coor, shopName);
        }
    }

    /**
     * 找钱界面是否显示
     *
     * @return
     */
    public Boolean isFindForeground() {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null != fragment) {
            return ((FindMoneyFragment) fragment).getIsForeground();
        }
        return false;
    }

    public void exchangeCoupon(Integer goldType) {
        Bundle bundle = new Bundle();
        bundle.putInt("couponType", 1);
        bundle.putInt("goldType", goldType);
        openActivity(CouponExchangeActivity.class, bundle, 0);
    }

    public void clearRoutePlan() {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null != fragment) {
            ((FindMoneyFragment) fragment).clearRoutePlan();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mCurrentFragment instanceof RecommendFragment) {
                    if (((RecommendFragment) mCurrentFragment).canRecommendWebGoBack()) {
                        ((RecommendFragment) mCurrentFragment).recomendWebGoBack();
                    } else {
                        backToFindMoney();
                    }
                    return true;
                } else if (mCurrentFragment instanceof FindMoneyFragment) {
                    Long currentTime = System.currentTimeMillis();
                    if (1000 > (currentTime - lastKeyDown)) {
                        App.getAppInstance().exitApp();
                    } else {
                        showSnackbar(getResources().getString(R.string.double_click_exit));
                        lastKeyDown = currentTime;
                    }
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getFragmentManager().putFragment(outState, "currentFragment", mCurrentFragment);
        outState.putString("currentTag", mCurrentTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void chatLoginSuccess() {
        mPresenter.imSDKLogin();
    }

    @Override
    public void imSDKLoginSuccess() {
        //退到后台发送通知
        //初始化程序后台后消息推送
        PushUtil.getInstance(this);
        mPresenter.getConversation();
    }

    @Override
    public void onIMOffline() {
        JPushInterface.stopPush(App.getAppInstance());
        openActivityClear(MainActivity.class, null, 0);
    }

    @Override
    public void initConversations(List<TIMConversation> conversationList) {
        this.conversationList.clear();
        for (TIMConversation item : conversationList) {
            switch (item.getType()) {
                case C2C:
                case Group:
                    this.conversationList.add(new NormalConversation(item));
                    break;
            }
        }
    }

    @Override
    public void updateConversation(TIMMessage message) {
        if (null == message) {
            Fragment chatFragment = fm.findFragmentByTag("conversation");
            if (null != chatFragment) {
                ((ConversationFragment) chatFragment).updateConversions(conversationList);
            }
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System) {
            mPresenter.getGroupManageLastMessage();
            return;
        }
        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        NormalConversation conversation = new NormalConversation(message.getConversation());
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation c = iterator.next();
            if (conversation.equals(c)) {
                conversation = (NormalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
        updateUnreadConversationBubble();
    }

    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {
        if (null == groupManageConversation) {
            groupManageConversation = new GroupManageConversation(message);
            conversationList.add(groupManageConversation);
        } else {
            groupManageConversation.setLastMessage(message);
        }
        groupManageConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        updateUnreadConversationBubble();
    }

    @Override
    public void reRegisterApp() {
        if (null == SharedPreferencesUtils.getString(this, "isFirstOpen", "") || SharedPreferencesUtils.getString(this, "isFirstOpen", "").trim().length() <= 0) {
            if (!Utils.isNetUsable(MainActivity.this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            mPresenter.appRegister(Utils.getDeviceId(this), "182c79dbf8871689878b0de620006ea3", "7ed99e89c4831f169627b5d20a0020f7c1a9b026244e6364ac1c12a9fa2314fe");
        }
    }

    @Override
    public void onAppRegister(boolean flag) {
        if (flag) {
            mRefreshAccessTokenHandler.sendEmptyMessage(0);
            SharedPreferencesUtils.setString(this, "isFirstOpen", "true");
        }
    }

    @Override
    public void getRefreshToken() {
        if (!Utils.isNetUsable(MainActivity.this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getRefreshToken("password", Utils.getDeviceId(this), "8e2f773d58c51c3a1854c059af34b49f");
    }

    @Override
    public void refreshAccessToken() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stopRefreshAccessToken) {
                    try {
                        Thread.sleep(3600000);
                        mRefreshAccessTokenHandler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onGetBackgroundBag(List<CoinType> coinTypes) {
        if (coinTypes.size() == 0) {
//            backgroundBagRV.showEmptyView();
            showSnackbar("没有数据,下拉刷新");
        } else {
//            backgroundBagRV.hideEmptyView();
            bagAdapter.appendList(coinTypes);
        }
    }

    @Override
    public void onDismissBackgroundBag() {
        backgroundBagBottomSheetDialog.dismiss();
    }

    public void initBackgroundBag() {
        bagAdapter.clearList();
        isFirstLoadBag = true;
    }

    @Override
    public void updateAccessToken() {
        mRefreshAccessTokenHandler.sendEmptyMessage(0);
    }

    @Override
    public void setPresenter(MainContract.MainPresenter presenter) {
        mPresenter = presenter;
    }

    private void imLogin() {
        if (SharedPreferencesUtils.getString(this, "im_account", "").length() > 0 && SharedPreferencesUtils.getString(this, "im_password", "").length() > 0) {
            String im_name = SharedPreferencesUtils.getString(this, "im_account", "");
            String im_pwd = SharedPreferencesUtils.getString(this, "im_password", "");
            mPresenter.chatLogin(im_name, im_pwd);
        }
    }

    public void chatLogin() {
        imLogin();
    }

    public void updateUnreadConversationBubble() {
        if (null == unreadConversationBubbleTV) return;
        long unreadCount = getTotalConversationUnreadNum();
        if (unreadCount <= 0) {
            unreadConversationBubbleTV.setVisibility(View.INVISIBLE);
        } else {
            unreadConversationBubbleTV.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unreadCount);
            unreadConversationBubbleTV.setBackgroundResource(R.mipmap.tecent_point1);
            if (unreadCount > 99) {
                unreadConversationBubbleTV.setBackgroundResource(R.mipmap.tecent_point2);
                unReadStr = getResources().getString(R.string.time_more);
            }
            unreadConversationBubbleTV.setText(unReadStr);
        }
    }

    /**
     * 统计未读信息数
     *
     * @return
     */
    private long getTotalConversationUnreadNum() {
        long num = 0;
        pgConversationList.clear();
        for (Conversation conversation : conversationList) {
            if (null != conversation.getType()) {
                switch (conversation.getType()) {
                    case C2C:
                        pgConversationList.add(conversation);
                        break;
                    case Group:
                        if (GroupInfo.getInstance().isInGroup(conversation.getIdentify())) {
                            pgConversationList.add(conversation);
                        }
                        break;
                }
            }
        }
        for (Conversation conversation : pgConversationList) {
            num += conversation.getUnreadNum();
        }
        return num;
    }

    public void showBackgroundBag() {
        backgroundBagBottomSheetDialog.show();
        if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0) {
            ButterKnife.apply(backgroundBagLoginCheck, BUTTERKNIFEVISIBLE);
            ButterKnife.apply(backgroundBagRV, BUTTERKNIFEGONE);
            bagAdapter.clearList();
            if (isFirstLoadBag) {
                isFirstLoadBag = false;
            }
        } else {
            ButterKnife.apply(backgroundBagLoginCheck, BUTTERKNIFEGONE);
            ButterKnife.apply(backgroundBagRV, BUTTERKNIFEVISIBLE);
            if (isFirstLoadBag) {
                isFirstLoadBag = false;
                getBackgroundBag();
            }
        }
    }

    private void initBackgroundBagBottomSheetDialog() {
        backgroundBagBottomSheetDialog = new BottomSheetDialog(this, R.style.background_bag_dialog);
        View backgroundBagView = LayoutInflater.from(this).inflate(R.layout.background_bag_layout, null);
        backgroundBagLoginCheck = (TextView) backgroundBagView.findViewById(R.id.login_check);
        backgroundBagRV = (UltimateRecyclerView) backgroundBagView.findViewById(R.id.background_bag_list);
        backgroundBagRV.setAdapter(bagAdapter);
        backgroundBagRV.setLayoutManager(mGridLayoutManager);
        backgroundBagRV.setHasFixedSize(true);
        backgroundBagRV.setSaveEnabled(true);
        backgroundBagRV.setClipToPadding(false);
        backgroundBagRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                backgroundBagRV.setRefreshing(false);
                bagAdapter.clearList();
                getBackgroundBag();
            }
        });
        backgroundBagView.findViewById(R.id.quit_background_bag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundBagBottomSheetDialog.dismiss();
            }
        });
        backgroundBagLoginCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_UPDATE_BAGCKGROUND_BAG_CODE);
            }
        });
        backgroundBagBottomSheetDialog.setContentView(backgroundBagView);
        backgroundBagBottomSheetDialog.setCancelable(false);
    }

    private void getBackgroundBag() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        if (SharedPreferencesUtils.getString(MainActivity.this, "baza_code", "").trim().length() > 0) {
            mPresenter.shopBackgroundBag(Constants.PERSONAL_BACKGROUND_BAG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(MainActivity.this, "baza_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
        } else {
            mPresenter.personalBackgroundBag(Constants.PERSONAL_BACKGROUND_BAG_ACTION_CODE, Constants.PERSONAL_WITHDRAW_ACTION_TYPE_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
        }
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void initJPush() {
        JPushInterface.init(App.getAppInstance());
        if (null == SharedPreferencesUtils.getString(this, "push_alias", "") || "".equals(SharedPreferencesUtils.getString(this, "push_alias", ""))) {
            if (null != SharedPreferencesUtils.getString(this, "baza_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "baza_code", ""))) {
                setAlias(SharedPreferencesUtils.getString(this, "baza_code", "").replaceAll("-", ""));
            } else if (null != SharedPreferencesUtils.getString(this, "cust_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "cust_code", ""))) {
                setAlias(SharedPreferencesUtils.getString(this, "cust_code", "").replaceAll("-", ""));
            } else {
                return;
            }
        }
        if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0)
            return;
        JPushInterface.resumePush(App.getAppInstance());
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
            switch (code) {
                case 0:
//                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    SharedPreferencesUtils.setString(MainActivity.this, "push_alias", alias);
                    break;
                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
//                    logs = "Failed with errorCode = " + code;
            }
//            showSnackbar(logs);
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAlias(getApplicationContext(),
                            (String) msg.obj,
                            mAliasCallback);
                    break;
                default:
                    break;
            }
        }
    };

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public void registerCustomMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("com.yzdsmart.Dingdingwen.CUSTOM_MESSAGE_RECEIVED_ACTION");
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.yzdsmart.Dingdingwen.CUSTOM_MESSAGE_RECEIVED_ACTION".equals(intent.getAction())) {
                String custom_title = intent.getStringExtra("custom_title");
                String custom_message = intent.getStringExtra("custom_message");
                String custom_content_type = intent.getStringExtra("custom_content_type");
                StringBuilder showMsg = new StringBuilder();
//                showMsg.append("Title:" + custom_title + "\n");
//                showMsg.append("Message:" + custom_message + "\n");
                showMsg.append(custom_message);
//                showMsg.append("Content Type:" + custom_content_type);
                if (null != custom_content_type && "ddwsys".equals(custom_content_type)) {
                    saveMessage(showMsg.toString());
                }
            }
        }
    }

    private void saveMessage(String message) {
        if (null != customMsgList) {
            customMsgList.add(message);
        }
    }

    private void showCustomMsg(String msg) {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null != fragment) {
            ((FindMoneyFragment) fragment).showCustomMessage(msg);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // EasyPermissions handles the request result.
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    @Override
//    public void onPermissionsGranted(int i, List<String> list) {
//
//    }
//
//    @Override
//    public void onPermissionsDenied(int i, List<String> list) {
//
//    }
//
//    @AfterPermissionGranted(REQUEST_LOCATION_PERM_CODE)
//    private void getLocationPermission() {
////        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.GET_ACCOUNTS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            EasyPermissions.requestPermissions(this, "应用需要手机定位权限", REQUEST_LOCATION_PERM_CODE, Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//    }
}
