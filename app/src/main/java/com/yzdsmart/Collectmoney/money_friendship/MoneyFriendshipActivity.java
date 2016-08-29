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
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.friend_future.FriendFutureActivity;
import com.yzdsmart.Collectmoney.money_friendship.conversation.ConversationFragment;
import com.yzdsmart.Collectmoney.money_friendship.friend_list.FriendListFragment;
import com.yzdsmart.Collectmoney.money_friendship.group_list.GroupListFragment;
import com.yzdsmart.Collectmoney.views.CustomNestRadioGroup;

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

//    private Long timeStampNow = 0l;//上次拉取的时间戳，默认:0
//    private Integer startIndex = 0;//下页拉取的起始位置
//    private Integer currentStandardSequence = 0;//上次拉取标配关系链的Sequence, 默认:0
//    private static final Integer PAGE_SIZE = 10;//每页获取的数量
//
//    private MoneyFriendshipContract.MoneyFriendshipPresenter mPresenter;
//
//    private LinearLayoutManager mLinearLayoutManager;
//    private List<Friendship> friendshipList;
//    private FriendshipAdapter friendshipAdapter;
//    private Paint dividerPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        friendshipList = new ArrayList<Friendship>();

        fm = getFragmentManager();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));

        imBottomTab.setOnCheckedChangeListener(this);

        initView();

//        new MoneyFriendshipPresenter(this, this);
//
//        mLinearLayoutManager = new LinearLayoutManager(this);
//        dividerPaint = new Paint();
//        dividerPaint.setStrokeWidth(1);
//        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
//        dividerPaint.setAntiAlias(true);
//        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
//        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();
//
//        friendshipAdapter = new FriendshipAdapter(this);
//        friendListRV.setHasFixedSize(true);
//        friendListRV.setLayoutManager(mLinearLayoutManager);
//        friendListRV.addItemDecoration(dividerItemDecoration);
//        friendListRV.setAdapter(friendshipAdapter);
//        //给每一个item设置前置的栏目条，
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(friendshipAdapter);
//        friendListRV.addItemDecoration(headersDecor);
//
//        List<Friendship> list = new ArrayList<Friendship>();
//        list.add(new Friendship("", "", "", "", "艾伦", "file:///android_asset/album_pic.png", null, null, 1, 2));
//        list.add(new Friendship("", "", "", "", "约翰", "file:///android_asset/album_pic.png", null, null, 2, 2));
//        list.add(new Friendship("", "", "", "", "比尔", "file:///android_asset/album_pic.png", null, null, 5, 1));
//        list.add(new Friendship("", "", "", "", "嗣位", "file:///android_asset/album_pic.png", null, null, 5, 1));
//        list.add(new Friendship("", "", "", "", "布朗", "file:///android_asset/album_pic.png", null, null, 3, 5));
//        list.add(new Friendship("", "", "", "", "韩梅梅", "file:///android_asset/album_pic.png", null, null, 2, 4));
//        list.add(new Friendship("", "", "", "", "汉斯", "file:///android_asset/album_pic.png", null, null, 3, 2));
//        list.add(new Friendship("", "", "", "", "沙漏", "file:///android_asset/album_pic.png", null, null, 3, 1));
//        list.add(new Friendship("", "", "", "", "轻语", "file:///android_asset/album_pic.png", null, null, 4, 3));
//        list.add(new Friendship("", "", "", "", "漂流瓶", "file:///android_asset/album_pic.png", null, null, 5, 5));
//        list.add(new Friendship("", "", "", "", "李明", "file:///android_asset/album_pic.png", null, null, 6, 2));
//        list.add(new Friendship("", "", "", "", "汤姆", "file:///android_asset/album_pic.png", null, null, 7, 3));
//        list.add(new Friendship("", "", "", "", "木樨", "file:///android_asset/album_pic.png", null, null, 5, 5));
//        list.add(new Friendship("", "", "", "", "杰克", "file:///android_asset/album_pic.png", null, null, 1, 2));
//        list.add(new Friendship("", "", "", "", "提姆", "file:///android_asset/album_pic.png", null, null, 4, 1));
//
//        friendshipList.addAll(list);
//        friendshipAdapter.appendList(friendshipList);
//
//        mPresenter.getFriendsList("000000", SharedPreferencesUtils.getString(this, "cust_code", ""), timeStampNow, startIndex, currentStandardSequence, PAGE_SIZE);
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
                openActivity(FriendFutureActivity.class);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void onGetFriendsList(List<Friendship> friends) {
//        friendshipAdapter.appendList(friends);
    }

    @Override
    public void setPresenter(MoneyFriendshipContract.MoneyFriendshipPresenter presenter) {
//        mPresenter = presenter;
    }

    @Override
    public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
        Fragment fragment;
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
                ButterKnife.apply(titleRightOpeLayout, BUTTERKNIFEVISIBLE);
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.user_add_icon));
                fragment = fm.findFragmentByTag("friend_list");
                if (null == fragment) {
                    fragment = new FriendListFragment();
                }
                addOrShowFragment(fragment, "friend_list");
                break;
            case R.id.group_list_radio:
                ButterKnife.apply(titleRightOpeLayout, BUTTERKNIFEVISIBLE);
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
}
