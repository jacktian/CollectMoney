package com.yzdsmart.Collectmoney.publish_tasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/4.
 */
public class PublishTasksActivity extends BaseActivity implements PublishTasksContract.PublishTasksView {
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
    @BindView(R.id.total_coin_counts)
    EditText totalCoinCountsET;
    @Nullable
    @BindView(R.id.min_coin_counts)
    EditText minCoinCountsET;
    @Nullable
    @BindView(R.id.max_coin_counts)
    EditText maxCoinCountsET;
    @Nullable
    @BindView(R.id.begin_time)
    EditText beginTimeET;
    @Nullable
    @BindView(R.id.end_time)
    EditText endTimeET;

    private PublishTasksContract.PublishTasksPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("发布任务");

        new PublishTasksPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_publish_tasks;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.publish_task})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.publish_task:
                Integer totalCoinCounts = Integer.valueOf(totalCoinCountsET.getText().toString());
                Integer minCoinCounts = Integer.valueOf(minCoinCountsET.getText().toString());
                Integer maxCoinCounts = Integer.valueOf(maxCoinCountsET.getText().toString());
                String beginTime = beginTimeET.getText().toString();
                String endTime = endTimeET.getText().toString();
                mPresenter.publishTask("000000", SharedPreferencesUtils.getString(this, "baza_code", ""), totalCoinCounts, minCoinCounts, maxCoinCounts, beginTime, endTime);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void setPresenter(PublishTasksContract.PublishTasksPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPublishTask(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        closeActivity();
    }
}
