package com.yzdsmart.Collectmoney.personal_friend_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.TIMConversationType;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.chat.ChatActivity;
import com.yzdsmart.Collectmoney.galley.UploadGalleyActivity;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.listener.AppBarOffsetChangeListener;
import com.yzdsmart.Collectmoney.login.LoginActivity;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.tecent_im.bean.UserInfo;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/8/19.
 */
public class PersonalFriendDetailActivity extends BaseActivity implements PersonalFriendDetailContract.PersonalFriendDetailView {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title, R.id.title_logo})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.appbarLayout)
    AppBarLayout appbarLayout;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.name_qr_layout)
    RelativeLayout nameQRLayout;
    @Nullable
    @BindView(R.id.im_ope_layout)
    RelativeLayout imOpeLayout;
    @Nullable
    @BindView(R.id.user_avater)
    CircleImageView userAvaterIV;
    @Nullable
    @BindView(R.id.user_name)
    TextView userNameTV;
    @Nullable
    @BindView(R.id.user_address)
    TextView userAddressTV;
    @Nullable
    @BindView(R.id.user_level)
    LinearLayout userLevelLayout;
    @Nullable
    @BindView(R.id.user_account)
    TextView userAccountTV;
    @Nullable
    @BindView(R.id.change_money_times)
    TextView changeMoneyTimesTV;
    @Nullable
    @BindView(R.id.coin_counts)
    TextView coinCountsTV;
    @Nullable
    @BindView(R.id.get_from_friend_counts)
    TextView getFriendCountsTV;

    private static final Integer REQUEST_LOGIN_CODE = 1000;

    private Integer type;//0 个人 1 好友
    private String friend_c_code;
    private String friend_identify;

    private PersonalFriendDetailContract.PersonalFriendDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getExtras().getInt("type");
        friend_c_code = getIntent().getExtras().getString("cust_code");
        friend_identify = "yzd" + getIntent().getExtras().getString("user_code");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        switch (type) {
            case 0:
                ButterKnife.apply(imOpeLayout, BUTTERKNIFEGONE);
                break;
            case 1:
                ButterKnife.apply(nameQRLayout, BUTTERKNIFEGONE);
                break;
        }

        appbarLayout.addOnOffsetChangedListener(new AppBarOffsetChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
                    titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.menu_icon_white));
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
                    titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.menu_icon));
                } else {
                    //中间状态
                }
            }
        });

        new PersonalFriendDetailPresenter(this, this);

        switch (type) {
            case 0:
                mPresenter.getCustLevel(SharedPreferencesUtils.getString(this, "cust_code", ""), "000000");
                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
                break;
            case 1:
                mPresenter.getCustInfo("000000", friend_c_code);
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_personal_friend_detail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            MainActivity.getInstance().chatLogin();
        }
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.msg_chat, R.id.personal_galley_title})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.msg_chat:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, REQUEST_LOGIN_CODE);
                    return;
                }
                if (friend_identify.trim().length() <= 3) return;
                bundle = new Bundle();
                bundle.putString("identify", friend_identify);
                bundle.putSerializable("type", TIMConversationType.C2C);
                openActivity(ChatActivity.class, bundle, 0);
                break;
            case R.id.personal_galley_title:
                switch (type) {
                    case 0:
                        bundle = new Bundle();
                        bundle.putInt("type", 0);
                        openActivity(UploadGalleyActivity.class, bundle, 0);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onGetCustLevel(Integer gra, Integer sta) {
        userLevelLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView diamond;
        for (int i = 0; i < sta; i++) {
            diamond = new ImageView(this);
            diamond.setLayoutParams(params);
            diamond.setImageDrawable(getResources().getDrawable(R.mipmap.diamond_pink));
            userLevelLayout.addView(diamond);
        }
    }

    @Override
    public void onGetCustInfo(CustInfoRequestResponse response) {
        Glide.with(this).load(response.getImageUrl()).error(getResources().getDrawable(R.mipmap.user_avater)).into(userAvaterIV);
        if (null != response.getCName() && !"".equals(response.getCName())) {
            userNameTV.setText(response.getCName());

        } else if (null != response.getNickName() && !"".equals(response.getNickName())) {
            userNameTV.setText(response.getNickName());
        } else {
            userNameTV.setText(response.getC_UserCode());
        }
        userNameTV.setText("".equals(response.getCName()) ? response.getC_UserCode() : response.getCName());
        userAddressTV.setText(response.getArea());
        userAccountTV.setText(response.getC_UserCode());
        changeMoneyTimesTV.setText("" + response.getOperNum());
        coinCountsTV.setText("" + response.getGoldNum());
        getFriendCountsTV.setText("" + response.getFriendNum());
    }

    @Override
    public void setPresenter(PersonalFriendDetailContract.PersonalFriendDetailPresenter presenter) {
        mPresenter = presenter;
    }
}
