package com.yzdsmart.Collectmoney.friend_future;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/20.
 */
public class FriendFutureActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.center_title,R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.left_title)
    TextView leftTitleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews,BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        leftTitleTV.setText(getResources().getString(R.string.future_search_new_friend));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_friend_future;
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
