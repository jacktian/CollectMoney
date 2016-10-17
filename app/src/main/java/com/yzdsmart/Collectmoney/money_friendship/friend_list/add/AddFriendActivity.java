package com.yzdsmart.Collectmoney.money_friendship.friend_list.add;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.bean.FriendshipInfo;
import com.yzdsmart.Collectmoney.tecent_im.views.LineControllerView;
import com.yzdsmart.Collectmoney.tecent_im.views.ListPickerDialog;
import com.yzdsmart.Collectmoney.tecent_im.views.NotifyDialog;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * 申请添加好友界面
 */
public class AddFriendActivity extends BaseActivity implements View.OnClickListener, AddFriendContract.AddFriendView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_left_operation_layout)
    FrameLayout titleLeftOpeLayout;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;

    private TextView tvName, btnAdd;
    private EditText editRemark, editMessage;
    private LineControllerView idField, groupField;
    private AddFriendContract.AddFriendPresenter mPresenter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText("详细资料");
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        titleLeftOpeLayout.setOnClickListener(this);

        tvName = (TextView) findViewById(R.id.name);
        idField = (LineControllerView) findViewById(R.id.id);
        id = getIntent().getStringExtra("id");
        tvName.setText(getIntent().getStringExtra("name"));
        idField.setContent(id);
        groupField = (LineControllerView) findViewById(R.id.group);
        btnAdd = (TextView) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        editMessage = (EditText) findViewById(R.id.editMessage);
        editRemark = (EditText) findViewById(R.id.editNickname);

        new AddFriendPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.btnAdd:
                mPresenter.addFriend(id, editRemark.getText().toString(), groupField.getContent().equals(getString(R.string.default_group_name)) ? "" : groupField.getContent(), editMessage.getText().toString());
                break;
            case R.id.group:
                final String[] groups = FriendshipInfo.getInstance().getGroupsArray();
                for (int i = 0; i < groups.length; ++i) {
                    if (groups[i].equals("")) {
                        groups[i] = getString(R.string.default_group_name);
                        break;
                    }
                }
                new ListPickerDialog().show(groups, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        groupField.setContent(groups[which]);
                    }
                });
                break;
        }
    }

    /**
     * 添加好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onAddFriend(TIMFriendStatus status) {
        switch (status) {
            case TIM_ADD_FRIEND_STATUS_PENDING:
                Toast.makeText(this, getResources().getString(R.string.add_friend_succeed), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_FRIEND_STATUS_SUCC:
                Toast.makeText(this, getResources().getString(R.string.add_friend_added), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                Toast.makeText(this, getResources().getString(R.string.add_friend_refuse_all), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                Toast.makeText(this, getResources().getString(R.string.add_friend_to_blacklist), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.add_friend_del_black_list), getFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.delBlackList(Collections.singletonList(id), new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(AddFriendActivity.this, getResources().getString(R.string.add_friend_del_black_err), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                Toast.makeText(AddFriendActivity.this, getResources().getString(R.string.add_friend_del_black_succ), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            default:
                Toast.makeText(this, getResources().getString(R.string.add_friend_error), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void setPresenter(AddFriendContract.AddFriendPresenter presenter) {
        mPresenter = presenter;
    }
}
