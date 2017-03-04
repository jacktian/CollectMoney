package com.yzdsmart.Dingdingwen.main.recommend_news_details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2017/3/3.
 */

public class RecommendNewsDetailsActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.center_title, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.news_details)
    WebView newsDetailsWV;

    private static final String TAG = "RecommendNewsDetailsActivity";

    private WebSettings newsDetailsSettings;

    private String pageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));

        MobclickAgent.openActivityDurationTrack(false);

        if (null != savedInstanceState) {
            pageUrl = savedInstanceState.getString("pageUrl");
        } else {
            pageUrl = getIntent().getExtras().getString("pageUrl");
        }

        if (null != pageUrl && pageUrl.trim().length() > 0) {
            loadPage(pageUrl);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_recomend_news_details;
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("pageUrl", pageUrl);
        super.onSaveInstanceState(outState);
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                if (newsDetailsWV.canGoBack()) {
                    newsDetailsWV.goBack();
                } else {
                    closeActivity();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (newsDetailsWV.canGoBack()) {
                    newsDetailsWV.goBack();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("JavascriptInterface")
    private void loadPage(String path) {
        newsDetailsSettings = newsDetailsWV.getSettings();
        //设置编码
        newsDetailsSettings.setDefaultTextEncodingName("UTF-8");
        //启用支持javascript
        newsDetailsSettings.setJavaScriptEnabled(true);
        newsDetailsSettings.setAllowFileAccess(true);
        newsDetailsSettings.setAllowFileAccess(true);
        newsDetailsSettings.setSupportZoom(true);
        newsDetailsSettings.setAllowContentAccess(true);
        newsDetailsSettings.setUseWideViewPort(true);
//        newsDetailsSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        newsDetailsSettings.setLoadWithOverviewMode(true);
        newsDetailsWV.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        newsDetailsWV.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (100 == newProgress) {
                    hideProgressDialog();
                }
            }
        });
        newsDetailsWV.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                //如果不需要其他对点击链接事件的处理返回true，否则返回false
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideProgressDialog();
            }
        });
        newsDetailsWV.clearHistory();
        newsDetailsWV.clearCache(true);
        newsDetailsWV.loadUrl(path);
        showProgressDialog(R.drawable.loading, getResources().getString(R.string.loading));
    }
}
