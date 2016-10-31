package com.yzdsmart.Dingdingwen.money_friendship.group_list.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.money_friendship.group_list.choose.ChooseFriendActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * 创建群页面
 */
public class CreateGroupActivity extends BaseActivity implements CreateGroupContract.CreateGroupView {
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
    @BindView(R.id.title_left_operation_layout)
    FrameLayout titleLeftOpeLayout;

    TextView mAddMembers;
    EditText mInputView;
    String type;
    private final int CHOOSE_MEM_CODE = 100;

    private CreateGroupContract.CreateGroupPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = "Public";

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText("创建群");
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        titleLeftOpeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });

        new CreateGroupPresenter(this, this);

        mInputView = (EditText) findViewById(R.id.input_group_name);
        mAddMembers = (TextView) findViewById(R.id.btn_add_group_member);
        mAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInputView.getText().toString().equals("")) {
                    Toast.makeText(CreateGroupActivity.this, getString(R.string.create_group_need_name), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CreateGroupActivity.this, ChooseFriendActivity.class);
                    startActivityForResult(intent, CHOOSE_MEM_CODE);
                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_create_group;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CHOOSE_MEM_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                mPresenter.createGroup(mInputView.getText().toString(),
                        type,
                        data.getStringArrayListExtra("select"),
                        new TIMValueCallBack<String>() {
                            @Override
                            public void onError(int i, String s) {
                                if (i == 80001) {
                                    Toast.makeText(CreateGroupActivity.this, getString(R.string.create_group_fail_because_wording), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CreateGroupActivity.this, getString(R.string.create_group_fail), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onSuccess(String s) {
                                Toast.makeText(CreateGroupActivity.this, getString(R.string.create_group_succeed), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                );
            }
        }
    }

    @Override
    public void setPresenter(CreateGroupContract.CreateGroupPresenter presenter) {
        mPresenter = presenter;
    }
}
