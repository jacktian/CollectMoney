package com.yzdsmart.Dingdingwen.money_friendship;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMGroupPendencyItem;
import com.tencent.TIMMessage;
import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.friend_future.FriendFutureActivity;
import com.yzdsmart.Collectmoney.money_friendship.conversation.ConversationFragment;
import com.yzdsmart.Collectmoney.money_friendship.friend_list.FriendListFragment;
import com.yzdsmart.Collectmoney.money_friendship.group_list.GroupListFragment;
import com.yzdsmart.Collectmoney.money_friendship.group_list.search.SearchGroupActivity;
import com.yzdsmart.Collectmoney.money_friendship.group_list.create.CreateGroupActivity;
import com.yzdsmart.Collectmoney.money_friendship.recommend_friends.RecommendFriendsFragment;
import com.yzdsmart.Collectmoney.tecent_im.bean.Conversation;
import com.yzdsmart.Collectmoney.tecent_im.bean.CustomMessage;
import com.yzdsmart.Collectmoney.tecent_im.bean.GroupInfo;
import com.yzdsmart.Collectmoney.tecent_im.bean.GroupManageConversation;
import com.yzdsmart.Collectmoney.tecent_im.bean.MessageFactory;
import com.yzdsmart.Collectmoney.tecent_im.bean.NormalConversation;
import com.yzdsmart.Collectmoney.views.CustomNestRadioGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/18.
 */
public class MoneyFriendshipActivity extends BaseActivity implements MoneyFriendshipContract.MoneyFriendshipView, CustomNestRadioGroup.OnCheckedChangeListener {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_right_operation_layout)
    FrameLayout titleRightOpeLayout;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.friend_profile_list)
    UltimateRecyclerView friendListRV;
    @Nullable
    @BindView(R.id.im_bottom_tab)
    CustomNestRadioGroup imBottomTab;
    @Nullable
    @BindView(R.id.unread_conversation_bubble)
    TextView unreadConversationBubbleTV;
    @Nullable
    @BindView(R.id.unread_future_bubble)
    TextView unreadFutureBubbleTV;
    @Nullable
    @BindView(R.id.bubble_count)
    TextView titleRightBubbleTV;
    @Nullable
    @BindView(R.id.recommend_friend_radio_text)
    CheckedTextView recommendFriendCTV;
    @Nullable
    @BindView(R.id.conversation_radio_text)
    CheckedTextView conversationCTV;
    @Nullable
    @BindView(R.id.friend_list_radio_text)
    CheckedTextView friendListCTV;
    @Nullable
    @BindView(R.id.group_list_radio_text)
    CheckedTextView groupListCTV;

    private FragmentManager fm;
    private Fragment mCurrentFragment;

    private MoneyFriendshipContract.MoneyFriendshipPresenter mPresenter;

    private List<Conversation> conversationList;
    private List<Conversation> pgConversationList = null;//个人或者群消息
    private GroupManageConversation groupManageConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            mCurrentFragment = getFragmentManager().getFragment(savedInstanceState, "currentFragment");
        }

        conversationList = new LinkedList<Conversation>();
        pgConversationList = new ArrayList<Conversation>();

        fm = getFragmentManager();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));

        imBottomTab.setOnCheckedChangeListener(this);

        new MoneyFriendshipPresenter(this, this);

        initView();

        mPresenter.getConversation();
    }

    private void initView() {
        centerTitleTV.setText("附近");
        recommendFriendCTV.setChecked(true);
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = new RecommendFriendsFragment();
        ft.add(R.id.layout_frame, new ConversationFragment(), "conversation");
        ft.add(R.id.layout_frame, mCurrentFragment, "recommend");
        ft.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_money_friendship;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.title_right_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.title_right_operation_layout:
                if (mCurrentFragment instanceof FriendListFragment) {
                    openActivity(FriendFutureActivity.class);
                } else {
                    showMoveDialog(this);
                }
                break;
        }
    }

    private Dialog inviteDialog;
    private TextView joinGroup, createGroup;

    private void showMoveDialog(Context context) {
        inviteDialog = new Dialog(context, R.style.tecent_dialog);
        inviteDialog.setContentView(R.layout.tecent_contact_more);
        joinGroup = (TextView) inviteDialog.findViewById(R.id.join_group);
        createGroup = (TextView) inviteDialog.findViewById(R.id.create_group);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(SearchGroupActivity.class);
                inviteDialog.dismiss();
            }
        });
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(CreateGroupActivity.class);
                inviteDialog.dismiss();
            }
        });
        Window window = inviteDialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        inviteDialog.show();
    }

    @Override
    public void setPresenter(MoneyFriendshipContract.MoneyFriendshipPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadConversationBubble();
        updateUnreadFriendFutureBubble();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unRegisterObserver();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getFragmentManager().putFragment(outState, "currentFragment", mCurrentFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < imBottomTab.getChildCount(); i++) {
            RadioButton mTab = (RadioButton) (((RelativeLayout) imBottomTab.getChildAt(i)).getChildAt(0));
            Fragment fragment = fm.findFragmentByTag((String) mTab.getTag());
            if (null != fragment) {
                if (!mTab.isChecked()) {
                    ft.hide(fragment);
                }
            }
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
        Fragment fragment;
        ButterKnife.apply(titleRightOpeLayout, BUTTERKNIFEVISIBLE);
        recommendFriendCTV.setChecked(false);
        conversationCTV.setChecked(false);
        friendListCTV.setChecked(false);
        groupListCTV.setChecked(false);
        switch (group.getCheckedRadioButtonId()) {
            case R.id.recommend_friend_radio:
                centerTitleTV.setText("附近");
                recommendFriendCTV.setChecked(true);
                ButterKnife.apply(titleRightOpeLayout, BUTTERKNIFEGONE);
                fragment = fm.findFragmentByTag("recommend");
                if (null == fragment) {
                    fragment = new RecommendFriendsFragment();
                }
                addOrShowFragment(fragment, "recommend");
                break;
            case R.id.conversation_radio:
                centerTitleTV.setText("消息");
                conversationCTV.setChecked(true);
                ButterKnife.apply(titleRightOpeLayout, BUTTERKNIFEGONE);
                fragment = fm.findFragmentByTag("conversation");
                if (null == fragment) {
                    fragment = new ConversationFragment();
                }
                addOrShowFragment(fragment, "conversation");
                break;
            case R.id.friend_list_radio:
                centerTitleTV.setText("通讯录");
                friendListCTV.setChecked(true);
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.user_add_icon));
                fragment = fm.findFragmentByTag("friend_list");
                if (null == fragment) {
                    fragment = new FriendListFragment();
                }
                addOrShowFragment(fragment, "friend_list");
                break;
            case R.id.group_list_radio:
                centerTitleTV.setText("群聊");
                groupListCTV.setChecked(true);
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.user_add_icon));
                fragment = fm.findFragmentByTag("group_list");
                if (null == fragment) {
                    fragment = new GroupListFragment();
                }
                addOrShowFragment(fragment, "group_list");
                break;
        }
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
        ft.commit();
        mCurrentFragment = fragment;
    }

    /**
     * 初始化界面或刷新界面
     *
     * @param conversationList
     */
    @Override
    public void initView(List<TIMConversation> conversationList) {
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

    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
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
        refreshConversation();
    }

    /**
     * 获取群管理最后一条系统消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
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
        refreshConversation();
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(identify)) {
                iterator.remove();
                refreshConversation();
                return;
            }
        }
    }

    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {
        for (Conversation conversation : conversationList) {
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId())) {
                String name = info.getGroupInfo().getGroupName();
                if (name.equals("")) {
                    name = info.getGroupInfo().getGroupId();
                }
                conversation.setName(name);
                refreshConversation();
                return;
            }
        }
    }

    /**
     * 刷新
     */
    @Override
    public void refreshConversation() {
        Fragment chatFragment = fm.findFragmentByTag("conversation");
        if (null != chatFragment) {
            ((ConversationFragment) chatFragment).updateConversions(conversationList);
        }
        updateUnreadConversationBubble();
    }

    @Override
    public void updateUnreadFriendFutureBubble() {
        mPresenter.getUnReadFriendFuture();
    }

    @Override
    public void onGetUnReadFriendFuture(Integer unreadCount) {
        if (null == unreadFutureBubbleTV || null == titleRightBubbleTV) return;
        if (unreadCount <= 0) {
            unreadFutureBubbleTV.setVisibility(View.INVISIBLE);
            titleRightBubbleTV.setVisibility(View.GONE);
        } else {
            unreadFutureBubbleTV.setVisibility(View.VISIBLE);
            titleRightBubbleTV.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unreadCount);
            unreadFutureBubbleTV.setBackgroundResource(R.mipmap.tecent_point1);
            titleRightBubbleTV.setBackgroundResource(R.mipmap.tecent_point1);
            if (unreadCount > 99) {
                unreadFutureBubbleTV.setBackgroundResource(R.mipmap.tecent_point2);
                titleRightBubbleTV.setBackgroundResource(R.mipmap.tecent_point2);
                unReadStr = getResources().getString(R.string.time_more);
            }
            unreadFutureBubbleTV.setText(unReadStr);
            titleRightBubbleTV.setText(unReadStr);
        }
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
}
