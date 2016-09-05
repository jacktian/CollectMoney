package com.yzdsmart.Collectmoney.personal_coin_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.GetCoinsLog;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/5.
 */
public class PersonalCoinsActivity extends BaseActivity implements PersonalCoinsContract.PersonalCoinsView {
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
    @BindView(R.id.coin_list)
    RecyclerView coinListRV;

    private PersonalCoinsContract.PersonalCoinsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("获取金币日志列表");

        new PersonalCoinsPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_personal_coins;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.publish_task})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }

    @Override
    public void onGetCoinsLog(List<GetCoinsLog> logList) {

    }

    @Override
    public void setPresenter(PersonalCoinsContract.PersonalCoinsPresenter presenter) {
        mPresenter = presenter;
    }
}
