package com.yzdsmart.Dingdingwen.withdrawals;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.share_sdk.OnekeyShare;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by jacks on 2016/9/23.
 */

public class WithDrawActivity extends BaseActivity implements WithDrawContract.WithDrawView {
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
    @BindView(R.id.coin_counts)
    TextView coinCountsTV;
    @Nullable
    @BindView(R.id.withdraw_gold_num)
    EditText withdrawGoldNumET;
    @Nullable
    @BindView(R.id.withdraw_rmb)
    TextView withdrawRMBTV;
    @Nullable
    @BindView(R.id.gold_rmb_ratio)
    TextView goldRMBRatioTV;
    @Nullable
    @BindView(R.id.withdraw_money)
    Button withdrawMoneyBtn;

    private Float GOLD_FORMAT_RMB_RATIO = 0.0f;

    private Integer userType;//0 个人 1 商家

    private WithDrawContract.WithDrawPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable shopWithdrawSuccessRunnable;
    private Runnable personalWithdrawSuccessRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        userType = bundle.getInt("userType");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        switch (userType) {
            case 0:
                centerTitleTV.setText("个人提现");
                break;
            case 1:
                centerTitleTV.setText("商家提现");
                GOLD_FORMAT_RMB_RATIO = 0.94f;
                break;
        }
        goldRMBRatioTV.setText("1金币=" + GOLD_FORMAT_RMB_RATIO + "元");

        new WithDrawPresenter(this, this);

        ShareSDK.initSDK(this);

        switch (userType) {
            case 0:
                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
                break;
            case 1:
                mPresenter.getLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""));
                break;
        }

        shopWithdrawSuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                mPresenter.getLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(WithDrawActivity.this, "baza_code", ""));
            }
        };
        personalWithdrawSuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(WithDrawActivity.this, "cust_code", ""));
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(shopWithdrawSuccessRunnable);
        mHandler.removeCallbacks(personalWithdrawSuccessRunnable);
        super.onDestroy();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.withdraw_money, R.id.with_all})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.withdraw_money:
                if (!requiredVerify(withdrawGoldNumET)) {
                    withdrawGoldNumET.setError(getResources().getString(R.string.input_withdraw_gold_num));
                    return;
                }
                switch (userType) {
                    case 0:
                        mPresenter.personalWithdrawCoins(Constants.PERSONAL_WITHDRAW_ACTION_CODE, Constants.PERSONAL_WITHDRAW_ACTION_TYPE_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), Integer.valueOf(withdrawGoldNumET.getText().toString()));
                        break;
                    case 1:
                        mPresenter.shopWithdrawCoins(Constants.SHOP_WITHDRAW_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), Integer.valueOf(withdrawGoldNumET.getText().toString()));
                        break;
                }
                break;
            case R.id.with_all:
                if (Float.valueOf(coinCountsTV.getText().toString()) > 0) {
                    withdrawGoldNumET.setText(coinCountsTV.getText().toString());
                    withdrawMoneyBtn.setEnabled(true);
                }
                break;
        }
    }

    @Optional
    @OnTextChanged({R.id.withdraw_gold_num})
    void onTextChanged() {
        if (withdrawGoldNumET.getText().toString().length() > 0) {
            switch (userType) {
                case 0:
                    if (Float.valueOf(withdrawGoldNumET.getText().toString()) > 100) {
                        GOLD_FORMAT_RMB_RATIO = 0.9f;
                    } else {
                        GOLD_FORMAT_RMB_RATIO = 0.8f;
                    }
                    break;
            }
            goldRMBRatioTV.setText("1金币=" + GOLD_FORMAT_RMB_RATIO + "元");
            withdrawMoneyBtn.setEnabled(true);
            DecimalFormat df = new DecimalFormat("#.00");
            withdrawRMBTV.setText("￥" + df.format((Float.valueOf(withdrawGoldNumET.getText().toString()) * GOLD_FORMAT_RMB_RATIO)));
        } else {
            withdrawRMBTV.setText("￥0.0");
            withdrawMoneyBtn.setEnabled(false);
        }
    }

    @Override
    public void onGetCustInfo(CustInfoRequestResponse response) {
        coinCountsTV.setText("" + response.getGoldNum());
    }

    @Override
    public void onGetLeftCoins(Integer counts) {
        coinCountsTV.setText("" + counts);
    }

    @Override
    public void onShopWithdrawCoins(String withdrawRMB) {
        withdrawGoldNumET.setText("");
        withdrawRMBTV.setText("");
        showProgressDialog(R.drawable.success, "您已成功提现" + withdrawRMB + "元");
        mHandler.postDelayed(shopWithdrawSuccessRunnable, 500);
        showShare();
    }

    @Override
    public void onPersonalWithdrawCoins(String withdrawRMB) {
        withdrawGoldNumET.setText("");
        withdrawRMBTV.setText("");
        showProgressDialog(R.drawable.success, "您已成功提现" + withdrawRMB + "元");
        mHandler.postDelayed(personalWithdrawSuccessRunnable, 500);
        showShare();
    }

    @Override
    public void setPresenter(WithDrawContract.WithDrawPresenter presenter) {
        mPresenter = presenter;
    }

    /**
     * 显示分享九宫格
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
