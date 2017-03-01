package com.yzdsmart.Dingdingwen.main.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.BaseFragment;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.ExpandListRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    @BindView(R.id.recommend_news)
    UltimateRecyclerView recommendNewsRV;
//    @Nullable
//    @BindView(R.id.recommend_webview)
//    WebView recommendWebView;

    private WebSettings recommendSettings;

    private static final String TAG = "RecommendFragment";

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private RecommendContract.RecommendPresenter mPresenter;
//
//    private String[] cities;
//    private ArrayAdapter<String> citiesAdapter;
//
//    private LinearLayoutManager mLinearLayoutManager;
//    private List<ExpandListRequestResponse> expandList;
//    private RecommendAdapter recommendAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RecommendPresenter(getActivity(), this);
//
//        cities = getResources().getStringArray(R.array.city_list);
//        citiesAdapter = new ArrayAdapter<String>(getActivity(),
//                R.layout.cities_list_item, cities);
//
//        mLinearLayoutManager = new LinearLayoutManager(getActivity());
//        expandList = new ArrayList<ExpandListRequestResponse>();
//        recommendAdapter = new RecommendAdapter(getActivity());
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
    public void onGetExpandList(List<ExpandListRequestResponse> expands) {

    }

    @Override
    public void setPresenter(RecommendContract.RecommendPresenter presenter) {
        mPresenter = presenter;
    }
}
