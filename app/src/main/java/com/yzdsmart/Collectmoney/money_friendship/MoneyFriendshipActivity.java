package com.yzdsmart.Collectmoney.money_friendship;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.friend_future.FriendFutureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/18.
 */
public class MoneyFriendshipActivity extends BaseActivity implements MoneyFriendshipContract.MoneyFriendshipView {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.friend_profile_list)
    UltimateRecyclerView friendListRV;

    private Long timeStampNow = 0l;//上次拉取的时间戳，默认:0
    private Integer startIndex = 0;//下页拉取的起始位置
    private Integer currentStandardSequence = 0;//上次拉取标配关系链的Sequence, 默认:0
    private static final Integer PAGE_SIZE = 10;//每页获取的数量

    private MoneyFriendshipContract.MoneyFriendshipPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<Friendship> friendshipList;
    private FriendshipAdapter friendshipAdapter;
    private Paint dividerPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendshipList = new ArrayList<Friendship>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.user_add_icon));

        new MoneyFriendshipPresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        friendshipAdapter = new FriendshipAdapter(this);
        friendListRV.setHasFixedSize(true);
        friendListRV.setLayoutManager(mLinearLayoutManager);
        friendListRV.addItemDecoration(dividerItemDecoration);
        friendListRV.setAdapter(friendshipAdapter);
        //给每一个item设置前置的栏目条，
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(friendshipAdapter);
        friendListRV.addItemDecoration(headersDecor);

        List<Friendship> list = new ArrayList<Friendship>();
        list.add(new Friendship("", "", "", "", "艾伦", "file:///android_asset/album_pic.png", null, null, 1, 2));
        list.add(new Friendship("", "", "", "", "约翰", "file:///android_asset/album_pic.png", null, null, 2, 2));
        list.add(new Friendship("", "", "", "", "比尔", "file:///android_asset/album_pic.png", null, null, 5, 1));
        list.add(new Friendship("", "", "", "", "嗣位", "file:///android_asset/album_pic.png", null, null, 5, 1));
        list.add(new Friendship("", "", "", "", "布朗", "file:///android_asset/album_pic.png", null, null, 3, 5));
        list.add(new Friendship("", "", "", "", "韩梅梅", "file:///android_asset/album_pic.png", null, null, 2, 4));
        list.add(new Friendship("", "", "", "", "汉斯", "file:///android_asset/album_pic.png", null, null, 3, 2));
        list.add(new Friendship("", "", "", "", "沙漏", "file:///android_asset/album_pic.png", null, null, 3, 1));
        list.add(new Friendship("", "", "", "", "轻语", "file:///android_asset/album_pic.png", null, null, 4, 3));
        list.add(new Friendship("", "", "", "", "漂流瓶", "file:///android_asset/album_pic.png", null, null, 5, 5));
        list.add(new Friendship("", "", "", "", "李明", "file:///android_asset/album_pic.png", null, null, 6, 2));
        list.add(new Friendship("", "", "", "", "汤姆", "file:///android_asset/album_pic.png", null, null, 7, 3));
        list.add(new Friendship("", "", "", "", "木樨", "file:///android_asset/album_pic.png", null, null, 5, 5));
        list.add(new Friendship("", "", "", "", "杰克", "file:///android_asset/album_pic.png", null, null, 1, 2));
        list.add(new Friendship("", "", "", "", "提姆", "file:///android_asset/album_pic.png", null, null, 4, 1));

        friendshipList.addAll(list);
        friendshipAdapter.appendList(friendshipList);

        mPresenter.getFriendsList("000000", "a9524621-6b74-42cc-b395-d7d521d5b4a4", timeStampNow, startIndex, currentStandardSequence, PAGE_SIZE);
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
        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void onGetFriendsList(List<Friendship> friends) {
        friendshipAdapter.appendList(friends);
    }

    @Override
    public void setPresenter(MoneyFriendshipContract.MoneyFriendshipPresenter presenter) {
        mPresenter = presenter;
    }
}
