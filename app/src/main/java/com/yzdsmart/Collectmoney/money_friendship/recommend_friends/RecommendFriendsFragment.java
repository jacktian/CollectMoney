package com.yzdsmart.Collectmoney.money_friendship.recommend_friends;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by YZD on 2016/9/25.
 */

public class RecommendFriendsFragment extends BaseFragment implements RecommendFriendsContract.RecommendFriendsView {
    @Nullable
    @BindView(R.id.recommend_friends_list)
    UltimateRecyclerView recommendFriendsRV;

    private static final String RECOMMEND_FRIEND_ACTION_CODE = "8566";
    private static final Integer RECOMMEND_NUM = 10;//获取数量

    private RecommendFriendsContract.RecommendFriendsPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<Friendship> friendshipList;
    private RecommendFriendsAdapter recommendFriendsAdapter;
    private Paint dividerPaint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendshipList = new ArrayList<Friendship>();

        new RecommendFriendsPresenter(getActivity(), this);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));

        recommendFriendsAdapter = new RecommendFriendsAdapter(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(getActivity()).paint(dividerPaint).build();
        recommendFriendsRV.setHasFixedSize(true);
        recommendFriendsRV.setLayoutManager(mLinearLayoutManager);
        recommendFriendsRV.addItemDecoration(dividerItemDecoration);
        recommendFriendsRV.setAdapter(recommendFriendsAdapter);
        recommendFriendsRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recommendFriendsRV.setRefreshing(false);
                recommendFriendsAdapter.clearList();
                mPresenter.getRecommendFriends(RECOMMEND_FRIEND_ACTION_CODE, "000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), RECOMMEND_NUM);
            }
        });

        mPresenter.getRecommendFriends(RECOMMEND_FRIEND_ACTION_CODE, "000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), RECOMMEND_NUM);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recommend_friends;
    }

    @Override
    public void onGetRecommendFriends(List<Friendship> friendships) {
        if (friendships.size() <= 0) return;
        friendshipList.clear();
        friendshipList.addAll(friendships);
        recommendFriendsAdapter.appendList(friendshipList);
    }

    @Override
    public void setPresenter(RecommendFriendsContract.RecommendFriendsPresenter presenter) {
        mPresenter = presenter;
    }
}
