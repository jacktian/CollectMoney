package com.yzdsmart.Dingdingwen.money_friendship.group_list.apply;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;

public class ApplyGroupActivity extends BaseActivity implements ApplyGroupContract.ApplyGroupView, TIMCallBack {
    private static final String TAG = "ApplyGroupActivity";

    private String identify;

    private ApplyGroupContract.ApplyGroupPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        identify = getIntent().getStringExtra("identify");

        new ApplyGroupPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        TextView des = (TextView) findViewById(R.id.description);
        des.setText("申请加入 " + identify);
        final EditText editText = (EditText) findViewById(R.id.input);
        TextView btnSend = (TextView) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.applyJoinGroup(identify, editText.getText().toString(), ApplyGroupActivity.this);
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_apply_group;
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
    public void onError(int i, String s) {
        if (i == 10013) {
            //已经是群成员
            Toast.makeText(this, getString(R.string.group_member_already), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, getResources().getString(R.string.send_success), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void setPresenter(ApplyGroupContract.ApplyGroupPresenter presenter) {
        mPresenter = presenter;
    }
}
