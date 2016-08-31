package com.yzdsmart.Collectmoney.money_friendship.group_list.apply;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;

public class ApplyGroupActivity extends BaseActivity implements ApplyGroupContract.ApplyGroupView, TIMCallBack {

    private String identify;

    private ApplyGroupContract.ApplyGroupPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        identify = getIntent().getStringExtra("identify");

        new ApplyGroupPresenter(this, this);

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
