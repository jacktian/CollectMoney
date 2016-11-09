package com.yzdsmart.Dingdingwen.friend_future;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.tencent.TIMFriendFutureItem;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.search_friend.SearchFriendActivity;
import com.yzdsmart.Dingdingwen.tecent_im.bean.FriendFuture;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/20.
 */
public class FriendFutureActivity extends BaseActivity implements FriendFutureContract.FriendFutureView {
    @Nullable
    @BindViews({R.id.center_title, R.id.title_logo})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.left_title)
    TextView leftTitleTV;
    @Nullable
    @BindView(R.id.friend_future_list)
    UltimateRecyclerView friendFutureRV;

    private static final int PAGE_SIZE = 10;
    private long pendSeq = 0, decideSeq = 0, recommendSeq = 0;

    private LinearLayoutManager mLinearLayoutManager;
    private List<FriendFuture> friendFutureList;
    private FriendFutureAdapter friendFutureAdapter;
    private Paint dividerPaint;

    private FriendFutureContract.FriendFuturePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendFutureList = new ArrayList<FriendFuture>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        leftTitleTV.setText(getResources().getString(R.string.future_search_new_friend));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.user_add_icon));

        new FriendFuturePresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        friendFutureAdapter = new FriendFutureAdapter(this);
        friendFutureRV.setHasFixedSize(true);
        friendFutureRV.setLayoutManager(mLinearLayoutManager);
        friendFutureRV.addItemDecoration(dividerItemDecoration);
        friendFutureRV.setAdapter(friendFutureAdapter);
        friendFutureRV.reenableLoadmore();
        friendFutureRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                getFutureFriends();
            }
        });
        friendFutureRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                friendFutureRV.setRefreshing(false);
                friendFutureRV.reenableLoadmore();
                pendSeq = 0;
                decideSeq = 0;
                recommendSeq = 0;
                friendFutureAdapter.clearList();
                getFutureFriends();
            }
        });

        getFutureFriends();
    }

    private void getFutureFriends() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getFutureFriends(PAGE_SIZE, pendSeq, decideSeq, recommendSeq);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_friend_future;
    }

    @Override
    protected void onDestroy() {
        mPresenter.unRegisterObserver();
        super.onDestroy();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.title_right_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.title_right_operation_layout:
                openActivity(SearchFriendActivity.class);
                break;
        }
    }

    @Override
    public void onGetFutureFriends(List<TIMFriendFutureItem> futureItems, long p, long d, long r) {
//        if (pendSeq == p && decideSeq == d && recommendSeq == r) {
//            friendFutureRV.disableLoadmore();
//            return;
//        }
        pendSeq = p;
        decideSeq = d;
        recommendSeq = r;
        if (futureItems.size() < PAGE_SIZE) {
            friendFutureRV.disableLoadmore();
        }
        if (futureItems.size() <= 0) return;
        friendFutureList.clear();
        for (TIMFriendFutureItem item : futureItems) {
            friendFutureList.add(new FriendFuture(item));
        }
        friendFutureAdapter.appendList(friendFutureList);
    }

    @Override
    public void refreshFriendFuture() {
        pendSeq = 0;
        decideSeq = 0;
        recommendSeq = 0;
        friendFutureAdapter.clearList();
        getFutureFriends();
    }

    @Override
    public void setPresenter(FriendFutureContract.FriendFuturePresenter presenter) {
        mPresenter = presenter;
    }
}
