package com.yzdsmart.Dingdingwen.main;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupPendencyItem;
import com.tencent.TIMMessage;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.main.find_money.FindMoneyFragment;
import com.yzdsmart.Dingdingwen.money_friendship.MoneyFriendshipActivity;
import com.yzdsmart.Dingdingwen.money_friendship.conversation.ConversationFragment;
import com.yzdsmart.Dingdingwen.personal.PersonalActivity;
import com.yzdsmart.Dingdingwen.register_login.login.LoginActivity;
import com.yzdsmart.Dingdingwen.tecent_im.bean.Conversation;
import com.yzdsmart.Dingdingwen.tecent_im.bean.CustomMessage;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupInfo;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupManageConversation;
import com.yzdsmart.Dingdingwen.tecent_im.bean.MessageFactory;
import com.yzdsmart.Dingdingwen.tecent_im.bean.NormalConversation;
import com.yzdsmart.Dingdingwen.tecent_im.bean.UserInfo;
import com.yzdsmart.Dingdingwen.tecent_im.service.TLSService;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by YZD on 2016/8/17.
 */
public class MainActivity extends BaseActivity implements MainContract.MainView, EasyPermissions.PermissionCallbacks {
    @Nullable
    @BindView(R.id.unread_conversation_bubble)
    TextView unreadConversationBubbleTV;

    private static final int REQUEST_LOCATION_PERM_CODE = 1111;//申请权限码

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

    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            mCurrentFragment = getFragmentManager().getFragment(savedInstanceState, "currentFragment");
            mCurrentTag = savedInstanceState.getString("currentTag");
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.layout_frame, mCurrentFragment, mCurrentTag);
            ft.commitAllowingStateLoss();
        }

        mainActivity = this;

        conversationList = new LinkedList<Conversation>();
        pgConversationList = new ArrayList<Conversation>();

        fm = getFragmentManager();

        initView();

//        getLocationPermission();//获取手机定位权限

        tlsService = TLSService.getInstance();

        new MainPresenter(this, this, tlsService);

        imLogin();

        initJPush();
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
        isForeground = true;
        updateUnreadConversationBubble();
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unRegisterObserver();
        JPushInterface.stopPush(App.getAppInstance());
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            imLogin();
        }
    }

    @Optional
    @OnClick({R.id.money_friend_radio_layout, R.id.money_friend_radio, R.id.loc_scan_coins, R.id.personal_radio_layout, R.id.personal_radio})
    void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.money_friend_radio_layout:
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
            case R.id.personal_radio_layout:
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

    public void planRoute(String coor) {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null != fragment) {
            ((FindMoneyFragment) fragment).planRoute(coor);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (!(mCurrentFragment instanceof FindMoneyFragment)) {
                    backToFindMoney();
                    return true;
                } else {
                    Long currentTime = System.currentTimeMillis();
                    if (1000 > (currentTime - lastKeyDown)) {
                        App.getAppInstance().exitApp();
                    } else {
                        showSnackbar(getResources().getString(R.string.double_click_exit));
                        lastKeyDown = currentTime;
                    }
                    return true;
                }
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
        mPresenter.getConversation();
        //退到后台发送通知
        //初始化程序后台后消息推送
//        PushUtil.getInstance();
    }

    @Override
    public void onIMOffline() {
        JPushInterface.stopPush(App.getAppInstance());
        App.getAppInstance().exitApp();
        openActivity(MainActivity.class);
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

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void initJPush() {
        JPushInterface.init(App.getAppInstance());
        JPushInterface.resumePush(App.getAppInstance());
        if (null == SharedPreferencesUtils.getString(this, "push_alias", "") || "".equals(SharedPreferencesUtils.getString(this, "push_alias", ""))) {
            if (null != SharedPreferencesUtils.getString(this, "baza_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "baza_code", ""))) {
                setAlias(SharedPreferencesUtils.getString(this, "baza_code", "").replaceAll("-", ""));
            } else if (null != SharedPreferencesUtils.getString(this, "cust_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "cust_code", ""))) {
                setAlias(SharedPreferencesUtils.getString(this, "cust_code", "").replaceAll("-", ""));
            } else {
                return;
            }
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {

    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERM_CODE)
    private void getLocationPermission() {
//        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.GET_ACCOUNTS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            EasyPermissions.requestPermissions(this, "应用需要手机定位权限", REQUEST_LOCATION_PERM_CODE, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
}
