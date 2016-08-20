package com.yzdsmart.Collectmoney.personal_friend_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/19.
 */
public class PersonalFriendDetailActivity extends BaseActivity{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        appbarLayout.addOnOffsetChangedListener(new AppBarOffsetChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
                    titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.menu_icon_white));
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
                    titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.menu_icon));
                }else {
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
    void onClick(View view){
        switch (view.getId()){
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }
}
