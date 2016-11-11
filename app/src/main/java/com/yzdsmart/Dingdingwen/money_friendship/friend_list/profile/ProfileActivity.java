package com.yzdsmart.Dingdingwen.money_friendship.friend_list.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMValueCallBack;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.chat.ChatActivity;
import com.yzdsmart.Dingdingwen.money_friendship.group_list.edit.EditActivity;
import com.yzdsmart.Dingdingwen.tecent_im.bean.FriendProfile;
import com.yzdsmart.Dingdingwen.tecent_im.bean.FriendshipInfo;
import com.yzdsmart.Dingdingwen.tecent_im.event.FriendshipEvent;
import com.yzdsmart.Dingdingwen.tecent_im.views.LineControllerView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity implements ProfileContract.ProfileView, View.OnClickListener {
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

    private static final String TAG = "ProfileActivity";

    private final int CHANGE_CATEGORY_CODE = 100;
    private final int CHANGE_REMARK_CODE = 200;

    private ProfileContract.ProfilePresenter mPresenter;
    private String identify, categoryStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        identify = getIntent().getStringExtra("identify");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText("详细资料");
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        titleLeftOpeLayout.setOnClickListener(this);

        new ProfilePresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        showProfile(identify);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
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

    /**
     * 显示用户信息
     *
     * @param identify
     */
    public void showProfile(final String identify) {
        final FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
        Log.d(TAG, "show profile isFriend " + (profile != null));
        if (profile == null) return;
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(profile.getName());
        CircleImageView avatar = (CircleImageView) findViewById(R.id.avatar);
        Glide.with(this).load(profile.getAvatarUrl()).error(getResources().getDrawable(R.mipmap.tecent_head_other)).into(avatar);
        LineControllerView id = (LineControllerView) findViewById(R.id.id);
        id.setContent(profile.getIdentify());
        final LineControllerView remark = (LineControllerView) findViewById(R.id.remark);
        remark.setContent(profile.getRemark());
        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditActivity.navToEdit(ProfileActivity.this, getString(R.string.profile_remark_edit), remark.getContent(), CHANGE_REMARK_CODE, new EditActivity.EditInterface() {
                    @Override
                    public void onEdit(String text, TIMCallBack callBack) {
                        mPresenter.setRemarkName(profile.getIdentify(), text, callBack);
                    }
                }, 20);

            }
        });
        LineControllerView category = (LineControllerView) findViewById(R.id.group);
        //一个用户可以在多个分组内，客户端逻辑保证一个人只存在于一个分组
        category.setContent(categoryStr = profile.getGroupName());
        LineControllerView black = (LineControllerView) findViewById(R.id.blackList);
        black.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPresenter.addBlackList(Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
                        @Override
                        public void onError(int i, String s) {
                            Log.e(TAG, "add black list error " + s);
                        }

                        @Override
                        public void onSuccess(List<TIMFriendResult> timFriendResults) {
                            if (timFriendResults.get(0).getStatus() == TIMFriendStatus.TIM_FRIEND_STATUS_SUCC) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.profile_black_succ), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.btnChat:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("identify", identify);
                intent.putExtra("type", TIMConversationType.C2C);
                startActivity(intent);
                finish();
                break;
            case R.id.btnDel:
                mPresenter.delFriend(identify);
                break;
            case R.id.group:
                final String[] groups = FriendshipInfo.getInstance().getGroupsArray();
                for (int i = 0; i < groups.length; ++i) {
                    if (groups[i].equals("")) {
                        groups[i] = getString(R.string.default_group_name);
                        break;
                    }
                }
//                new ListPickerDialog().show(groups, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (groups[which].equals(categoryStr)) return;
//                        friendshipManagerPresenter.changeFriendGroup(identify,
//                                categoryStr.equals(getString(R.string.default_group_name)) ? null : categoryStr,
//                                groups[which].equals(getString(R.string.default_group_name)) ? null : groups[which]);
//                    }
//                });
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_CATEGORY_CODE) {
            if (resultCode == RESULT_OK) {
                LineControllerView category = (LineControllerView) findViewById(R.id.group);
                category.setContent(categoryStr = data.getStringExtra("category"));
            }
        } else if (requestCode == CHANGE_REMARK_CODE) {
            if (resultCode == RESULT_OK) {
                LineControllerView remark = (LineControllerView) findViewById(R.id.remark);
                remark.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));

            }
        }

    }

    /**
     * 添加好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onAddFriend(TIMFriendStatus status) {

    }

    /**
     * 删除好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onDelFriend(TIMFriendStatus status) {
        switch (status) {
            case TIM_FRIEND_STATUS_SUCC:
                Toast.makeText(this, getResources().getString(R.string.profile_del_succeed), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_FRIEND_STATUS_UNKNOWN:
                Toast.makeText(this, getResources().getString(R.string.profile_del_fail), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /**
     * 修改好友分组回调
     *
     * @param status    返回状态
     * @param groupName 分组名
     */
    @Override
    public void onChangeGroup(TIMFriendStatus status, String groupName) {
        LineControllerView category = (LineControllerView) findViewById(R.id.group);
        if (groupName == null) {
            groupName = getString(R.string.default_group_name);
        }
        switch (status) {
            case TIM_FRIEND_STATUS_UNKNOWN:
                Toast.makeText(this, getString(R.string.change_group_error), Toast.LENGTH_SHORT).show();
            case TIM_FRIEND_STATUS_SUCC:
                category.setContent(groupName);
                FriendshipEvent.getInstance().OnFriendGroupChange();
                break;
            default:
                Toast.makeText(this, getString(R.string.change_group_error), Toast.LENGTH_SHORT).show();
                category.setContent(getString(R.string.default_group_name));
                break;
        }
    }

    @Override
    public void setPresenter(ProfileContract.ProfilePresenter presenter) {
        mPresenter = presenter;
    }
}
