package com.yzdsmart.Collectmoney.money_friendship.friend_list;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jacks on 2016/8/29.
 */
public class FriendListFragment extends BaseFragment implements FriendListContract.FriendListView {
    @Nullable
    @BindView(R.id.search_filter)
    EditText searchFilterET;
    @Nullable
    @BindView(R.id.friend_profile_list)
    UltimateRecyclerView friendProfileListRV;

    private Long timeStampNow = 0l;//上次拉取的时间戳，默认:0
    private Integer startIndex = 0;//下页拉取的起始位置
    private Integer currentStandardSequence = 0;//上次拉取标配关系链的Sequence, 默认:0
    private static final Integer PAGE_SIZE = 10;//每页获取的数量

    private FriendListContract.FriendListPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<Friendship> friendshipList;
    private FriendListAdapter friendListAdapter;
    private Paint dividerPaint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendshipList = new ArrayList<Friendship>();

        new FriendListPresenter(getActivity(), this);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));

        friendListAdapter = new FriendListAdapter(getActivity());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_friend_list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(getActivity()).paint(dividerPaint).build();
        friendProfileListRV.setHasFixedSize(true);
        friendProfileListRV.setLayoutManager(mLinearLayoutManager);
        friendProfileListRV.addItemDecoration(dividerItemDecoration);
        friendProfileListRV.setAdapter(friendListAdapter);
        //给每一个item设置前置的栏目条，
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(friendListAdapter);
        friendProfileListRV.addItemDecoration(headersDecor);
        friendProfileListRV.reenableLoadmore();
        friendProfileListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                mPresenter.getFriendsList("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), timeStampNow, startIndex, currentStandardSequence, PAGE_SIZE);
            }
        });
        mPresenter.getFriendsList("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), timeStampNow, startIndex, currentStandardSequence, PAGE_SIZE);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void onGetFriendsList(Long timeStampNow, Integer startIndex, Integer sequence, List<Friendship> friends) {
        this.timeStampNow = timeStampNow;
        this.startIndex = startIndex;
        this.currentStandardSequence = sequence;
        friendshipList.clear();
        friendshipList.addAll(friends);
        friendListAdapter.appendList(friendshipList);
    }

    @Override
    public void setPresenter(FriendListContract.FriendListPresenter presenter) {
        mPresenter = presenter;
    }
}
