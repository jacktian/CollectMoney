package com.yzdsmart.Dingdingwen.main.recommend;

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
import com.yzdsmart.Dingdingwen.BaseFragment;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.RecommendBannerRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RecommendNewsRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class RecommendFragment extends BaseFragment implements RecommendContract.RecommendView {

    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    //    @Nullable
//    @BindViews({R.id.city_recommend_spinner})
//    List<View> showViews;
//    @Nullable
//    @BindView(R.id.city_recommend_spinner)
//    BetterSpinner cityRecommendSpinner;
    @Nullable
    @BindView(R.id.recommend_items)
    UltimateRecyclerView recommendItemsRV;
//    @Nullable
//    @BindView(R.id.recommend_webview)
//    WebView recommendWebView;

//    private WebSettings recommendSettings;

    private static final String TAG = "RecommendFragment";

    private Integer bannerPageIndex = 1;
    private Integer bannerLastsequence = 0;
    private static final Integer BANNER_PAGE_SIZE = 5;

    private Integer newsPageIndex = 1;
    private Integer newsLastsequence = 0;
    private static final Integer NEWS_PAGE_SIZE = 3;

    private RecommendContract.RecommendPresenter mPresenter;
    //
//    private String[] cities;
//    private ArrayAdapter<String> citiesAdapter;
//
    private GridLayoutManager mGridLayoutManager;
    //    private List<ExpandListRequestResponse> expandList;
    private RecommendAdapter recommendAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RecommendPresenter(getActivity(), this);
//
//        cities = getResources().getStringArray(R.array.city_list);
//        citiesAdapter = new ArrayAdapter<String>(getActivity(),
//                R.layout.cities_list_item, cities);
//
        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
//        expandList = new ArrayList<ExpandListRequestResponse>();
        recommendAdapter = new RecommendAdapter(getActivity());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.apply(hideViews, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
//        ButterKnife.apply(showViews, ((BaseActivity) getActivity()).BUTTERKNIFEVISIBLE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText(getActivity().getResources().getString(R.string.recommend));
//        titleRightOpeTLIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.grey_mail_icon));

//        cityRecommendSpinner.setAdapter(citiesAdapter);
//        cityRecommendSpinner.setText(cities[0]);
//
//        recommendListRV.setHasFixedSize(true);
//        recommendListRV.setLayoutManager(mLinearLayoutManager);
//        recommendListRV.setAdapter(recommendAdapter);
//        recommendListRV.reenableLoadmore();
//        recommendListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                getExpandList();
//            }
//        });
//        recommendListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                recommendListRV.setRefreshing(false);
//                recommendListRV.reenableLoadmore();
//                pageIndex = 1;
//                recommendAdapter.clearList();
//                getExpandList();
//            }
//        });
//
//        getExpandList();

//        loadPage();

        recommendItemsRV.setAdapter(recommendAdapter);
        recommendItemsRV.setLayoutManager(mGridLayoutManager);
        recommendItemsRV.setHasFixedSize(true);
        recommendItemsRV.setSaveEnabled(true);
        recommendItemsRV.setClipToPadding(false);
        recommendItemsRV.reenableLoadmore();
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

    private void getRecommendBanner() {
        if (!Utils.isNetUsable(getActivity())) {
            ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getRecommendBanner("00000000", "88e1ed66aeaa4ec66dc203e0698800bd", bannerPageIndex, BANNER_PAGE_SIZE, bannerLastsequence, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
    }

    private void getRecommendNews() {
        if (!Utils.isNetUsable(getActivity())) {
            ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getRecommendNews("00000000", "303be61cba8d9e0b5c02aa3b859a7c77", newsPageIndex, NEWS_PAGE_SIZE, newsLastsequence, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
    }

//    @SuppressLint("JavascriptInterface")
//    private void loadPage() {
//        recommendSettings = recommendWebView.getSettings();
//        //设置编码
//        recommendSettings.setDefaultTextEncodingName("UTF-8");
//        //启用支持javascript
//        recommendSettings.setJavaScriptEnabled(true);
//        recommendSettings.setAllowFileAccess(true);
//        recommendSettings.setAllowFileAccess(true);
//        recommendSettings.setSupportZoom(true);
//        recommendSettings.setAllowContentAccess(true);
////        recommendSettings.setDomStorageEnabled(true);
//        recommendSettings.setUseWideViewPort(true);
//        recommendSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        recommendSettings.setLoadWithOverviewMode(true);
//        recommendWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
//        recommendWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (100 == newProgress) {
//                    ((MainActivity) getActivity()).hideProgressDialog();
//                }
//            }
//        });
//        recommendWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
//                view.loadUrl(url);
//                //如果不需要其他对点击链接事件的处理返回true，否则返回false
//                return true;
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                ((MainActivity) getActivity()).hideProgressDialog();
//            }
//        });
//        recommendWebView.clearHistory();
//        recommendWebView.clearCache(true);
//        recommendWebView.loadUrl("http://139.196.177.114:7288/discover/");
////        recommendWebView.loadUrl("https://www.baidu.com");
//        ((MainActivity) getActivity()).showProgressDialog(R.drawable.loading, getActivity().getResources().getString(R.string.loading));
//    }

    //    private void getExpandList() {
//        if (!Utils.isNetUsable(getActivity())) {
//            ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
//            return;
//        }
//        mPresenter.getExpandList("000000", pageIndex, PAGE_SIZE, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
//    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
//                if (recommendWebView.canGoBack()) {
//                    recommendWebView.goBack();
//                } else {
                ((MainActivity) getActivity()).backToFindMoney();
//                }
                break;
        }
    }

//    public boolean canRecommendWebGoBack() {
//        return recommendWebView.canGoBack();
//    }
//
//    public void recomendWebGoBack() {
//        recommendWebView.goBack();
//    }

    @Override
    public void onGetRecommendBanner(List<RecommendBannerRequestResponse.ListsBean> banners) {
        recommendAdapter.clearBannerList();
        recommendAdapter.appendBannerList(banners);
    }

    @Override
    public void onGetRecommendNews(List<RecommendNewsRequestResponse.ListsBean> news, Integer lastsequence) {
        newsLastsequence = lastsequence;
        newsPageIndex++;
        if (news.size() < NEWS_PAGE_SIZE) {
            recommendItemsRV.disableLoadmore();
        }
        if (0 >= news.size()) return;
        recommendAdapter.appendNewsList(news);
    }

    @Override
    public void setPresenter(RecommendContract.RecommendPresenter presenter) {
        mPresenter = presenter;
    }
}
