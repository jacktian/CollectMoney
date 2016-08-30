package com.yzdsmart.Collectmoney.money_friendship;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMGroupPendencyItem;
import com.tencent.TIMMessage;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.friend_future.FriendFutureActivity;
import com.yzdsmart.Collectmoney.money_friendship.conversation.ConversationFragment;
import com.yzdsmart.Collectmoney.money_friendship.friend_list.FriendListFragment;
import com.yzdsmart.Collectmoney.money_friendship.group_list.GroupListFragment;
import com.yzdsmart.Collectmoney.tecent_im.bean.Conversation;
import com.yzdsmart.Collectmoney.tecent_im.bean.CustomMessage;
import com.yzdsmart.Collectmoney.tecent_im.bean.GroupManageConversation;
import com.yzdsmart.Collectmoney.tecent_im.bean.MessageFactory;
import com.yzdsmart.Collectmoney.tecent_im.bean.NormalConversation;
import com.yzdsmart.Collectmoney.views.CustomNestRadioGroup;

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
    @BindViews({R.id.left_title, R.id.center_title, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_right_operation_layout)
    FrameLayout titleRightOpeLayout;
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

    private FragmentManager fm;
    private Fragment mCurrentFragment;

    private MoneyFriendshipContract.MoneyFriendshipPresenter mPresenter;

    private List<Conversation> conversationList;
    private GroupManageConversation groupManageConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationList = new LinkedList<>();

        fm = getFragmentManager();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));

        imBottomTab.setOnCheckedChangeListener(this);

        new MoneyFriendshipPresenter(this, this);

        initView();

        mPresenter.getConversation();
    }

    private void initView() {
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = new ConversationFragment();
        ft.add(R.id.layout_frame, mCurrentFragment, "conversation");
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
                if(mCurrentFragment instanceof FriendListFragment){
                    openActivity(FriendFutureActivity.class);
                }else{

                }
                break;
        }
    }

    @Override
    public void setPresenter(MoneyFriendshipContract.MoneyFriendshipPresenter presenter) {
        mPresenter = presenter;
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
    public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
        Fragment fragment;
        ButterKnife.apply(titleRightOpeLayout, BUTTERKNIFEVISIBLE);
        switch (group.getCheckedRadioButtonId()) {
            case R.id.conversation_radio:
                ButterKnife.apply(titleRightOpeLayout, BUTTERKNIFEGONE);
                fragment = fm.findFragmentByTag("conversation");
                if (null == fragment) {
                    fragment = new ConversationFragment();
                }
                addOrShowFragment(fragment, "conversation");
                break;
            case R.id.friend_list_radio:
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.user_add_icon));
                fragment = fm.findFragmentByTag("friend_list");
                if (null == fragment) {
                    fragment = new FriendListFragment();
                }
                addOrShowFragment(fragment, "friend_list");
                break;
            case R.id.group_list_radio:
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.plus_icon));
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
    }
}
