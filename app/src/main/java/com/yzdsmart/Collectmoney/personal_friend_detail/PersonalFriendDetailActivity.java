package com.yzdsmart.Collectmoney.personal_friend_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.listener.AppBarOffsetChangeListener;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/19.
 */
public class PersonalFriendDetailActivity extends BaseActivity {
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

    private Integer type;//0 个人 1 好友

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getExtras().getInt("type");

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
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_personal_friend_detail;
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
}
