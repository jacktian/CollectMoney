package com.yzdsmart.Dingdingwen.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.RecommendBannerRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RecommendNewsRequestResponse;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2017/3/6.
 */

public class RecommendActivity extends BaseActivity implements RecommendContract.RecommendView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.recommend_items)
    UltimateRecyclerView recommendItemsRV;

    private static final String TAG = "RecommendActivity";

    private Integer bannerPageIndex = 1;
    private Integer bannerLastsequence = 0;
    private static final Integer BANNER_PAGE_SIZE = 5;

    private Integer newsPageIndex = 1;
    private Integer newsLastsequence = 0;
    private static final Integer NEWS_PAGE_SIZE = 5;

    private RecommendContract.RecommendPresenter mPresenter;

    private GridLayoutManager mGridLayoutManager;
    private RecommendAdapter recommendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("推荐");

        new RecommendPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        mGridLayoutManager = new GridLayoutManager(this, 1);
        recommendAdapter = new RecommendAdapter(this);

        recommendItemsRV.setAdapter(recommendAdapter);
        recommendItemsRV.setLayoutManager(mGridLayoutManager);
        recommendItemsRV.setHasFixedSize(true);
        recommendItemsRV.setSaveEnabled(true);
        recommendItemsRV.setClipToPadding(false);
        recommendItemsRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                getRecommendNews();
            }
        });
        recommendItemsRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recommendItemsRV.setRefreshing(false);
                recommendItemsRV.reenableLoadmore();

                newsPageIndex = 1;
                newsLastsequence = 0;

                recommendAdapter.clearBannerList();
                recommendAdapter.clearNewsList();

                getRecommendBanner();
                getRecommendNews();
            }
        });

        getRecommendBanner();
        getRecommendNews();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_recommend;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }

    private void getRecommendBanner() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getRecommendBanner("00000000", "88e1ed66aeaa4ec66dc203e0698800bd", bannerPageIndex, BANNER_PAGE_SIZE, bannerLastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    private void getRecommendNews() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getRecommendNews("00000000", "303be61cba8d9e0b5c02aa3b859a7c77", newsPageIndex, NEWS_PAGE_SIZE, newsLastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    public void onGetRecommendBanner(List<RecommendBannerRequestResponse.ListsBean> recommendBanners) {
        recommendAdapter.clearBannerList();
        if (0 >= recommendBanners.size()) return;
        recommendAdapter.appendBannerList(recommendBanners);
        recommendItemsRV.scrollVerticallyToPosition(0);
    }

    @Override
    public void onGetRecommendNews(List<RecommendNewsRequestResponse.ListsBean> recommendNews, Integer lastsequence) {
        newsLastsequence = lastsequence;
        newsPageIndex++;
        recommendItemsRV.reenableLoadmore();
        if (recommendNews.size() < NEWS_PAGE_SIZE) {
            recommendItemsRV.disableLoadmore();
        }
        if (0 >= recommendNews.size()) return;
        recommendAdapter.appendNewsList(recommendNews);
    }

    @Override
    public void setPresenter(RecommendContract.RecommendPresenter presenter) {
        mPresenter = presenter;
    }
}
