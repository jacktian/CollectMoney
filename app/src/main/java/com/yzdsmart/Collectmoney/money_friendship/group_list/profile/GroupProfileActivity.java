package com.yzdsmart.Collectmoney.money_friendship.group_list.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupAddOpt;
import com.tencent.TIMGroupDetailInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMGroupReceiveMessageOpt;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.chat.ChatActivity;
import com.yzdsmart.Collectmoney.money_friendship.group_list.apply.ApplyGroupActivity;
import com.yzdsmart.Collectmoney.money_friendship.group_list.edit.EditActivity;
import com.yzdsmart.Collectmoney.money_friendship.group_list.member.GroupMemberActivity;
import com.yzdsmart.Collectmoney.tecent_im.bean.GroupInfo;
import com.yzdsmart.Collectmoney.tecent_im.bean.UserInfo;
import com.yzdsmart.Collectmoney.tecent_im.views.LineControllerView;
import com.yzdsmart.Collectmoney.tecent_im.views.ListPickerDialog;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class GroupProfileActivity extends BaseActivity implements GroupProfileContract.GroupProfileView, View.OnClickListener {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;

    private final String TAG = "GroupProfileActivity";

    private String identify, type;
    private TIMGroupDetailInfo info;
    private boolean isInGroup;
    private boolean isGroupOwner;
    private final int REQ_CHANGE_NAME = 100, REQ_CHANGE_INTRO = 200;
    private TIMGroupMemberRoleType roleType = TIMGroupMemberRoleType.NotMember;
    private Map<String, TIMGroupAddOpt> allowTypeContent;
    private Map<String, TIMGroupReceiveMessageOpt> messageOptContent;
    private LineControllerView name, intro;

    private GroupProfileContract.GroupProfilePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        identify = getIntent().getStringExtra("identify");
        isInGroup = GroupInfo.getInstance().isInGroup(identify);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("聊天设置");

        name = (LineControllerView) findViewById(R.id.nameText);
        intro = (LineControllerView) findViewById(R.id.groupIntro);
        LinearLayout controlInGroup = (LinearLayout) findViewById(R.id.controlInGroup);
        controlInGroup.setVisibility(isInGroup ? View.VISIBLE : View.GONE);
        TextView controlOutGroup = (TextView) findViewById(R.id.controlOutGroup);
        controlOutGroup.setVisibility(isInGroup ? View.GONE : View.VISIBLE);
        ((FrameLayout) findViewById(R.id.title_left_operation_layout)).setOnClickListener(this);

        new GroupProfilePresenter(this, this, isInGroup, Collections.singletonList(identify));

        mPresenter.getGroupDetailInfo();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_group_chat_setting;
    }

    /**
     * 显示群资料
     *
     * @param groupInfos 群资料信息列表
     */
    @Override
    public void showGroupInfo(List<TIMGroupDetailInfo> groupInfos) {
        info = groupInfos.get(0);
        isGroupOwner = info.getGroupOwner().equals(UserInfo.getInstance().getId());
        roleType = GroupInfo.getInstance().getRole(identify);
        type = info.getGroupType();
        LineControllerView member = (LineControllerView) findViewById(R.id.member);
        if (isInGroup) {
            member.setContent(String.valueOf(info.getMemberNum()));
            member.setOnClickListener(this);
        } else {
            member.setVisibility(View.GONE);
        }
        name.setContent(info.getGroupName());
        LineControllerView id = (LineControllerView) findViewById(R.id.idText);
        id.setContent(info.getGroupId());

        intro.setContent(info.getGroupIntroduction());
        LineControllerView opt = (LineControllerView) findViewById(R.id.addOpt);
        switch (info.getGroupAddOpt()) {
            case TIM_GROUP_ADD_AUTH:
                opt.setContent(getString(R.string.chat_setting_group_auth));
                break;
            case TIM_GROUP_ADD_ANY:
                opt.setContent(getString(R.string.chat_setting_group_all_accept));
                break;
            case TIM_GROUP_ADD_FORBID:
                opt.setContent(getString(R.string.chat_setting_group_all_reject));
                break;
        }
        LineControllerView msgNotify = (LineControllerView) findViewById(R.id.messageNotify);
        if (GroupInfo.getInstance().isInGroup(identify)) {
            switch (GroupInfo.getInstance().getMessageOpt(identify)) {
                case NotReceive:
                    msgNotify.setContent(getString(R.string.chat_setting_no_rev));
                    break;
                case ReceiveAndNotify:
                    msgNotify.setContent(getString(R.string.chat_setting_rev_notify));
                    break;
                case ReceiveNotNotify:
                    msgNotify.setContent(getString(R.string.chat_setting_rev_not_notify));
                    break;
            }
            msgNotify.setOnClickListener(this);
            messageOptContent = new HashMap<>();
            messageOptContent.put(getString(R.string.chat_setting_no_rev), TIMGroupReceiveMessageOpt.NotReceive);
            messageOptContent.put(getString(R.string.chat_setting_rev_not_notify), TIMGroupReceiveMessageOpt.ReceiveNotNotify);
            messageOptContent.put(getString(R.string.chat_setting_rev_notify), TIMGroupReceiveMessageOpt.ReceiveAndNotify);
        } else {
            msgNotify.setVisibility(View.GONE);
        }
        if (isManager()) {
            opt.setCanNav(true);
            opt.setOnClickListener(this);
            allowTypeContent = new HashMap<>();
            allowTypeContent.put(getString(R.string.chat_setting_group_auth), TIMGroupAddOpt.TIM_GROUP_ADD_AUTH);
            allowTypeContent.put(getString(R.string.chat_setting_group_all_accept), TIMGroupAddOpt.TIM_GROUP_ADD_ANY);
            allowTypeContent.put(getString(R.string.chat_setting_group_all_reject), TIMGroupAddOpt.TIM_GROUP_ADD_FORBID);
            name.setCanNav(true);
            name.setOnClickListener(this);
            intro.setCanNav(true);
            intro.setOnClickListener(this);
        }
        TextView btnDel = (TextView) findViewById(R.id.btnDel);
        btnDel.setText(isGroupOwner ? getString(R.string.chat_setting_dismiss) : getString(R.string.chat_setting_quit));

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.btnChat:
                bundle = new Bundle();
                bundle.putString("identify", identify);
                bundle.putSerializable("type", TIMConversationType.Group);
                intent = new Intent(GroupProfileActivity.this, ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnDel:
                if (isGroupOwner) {
                    mPresenter.dismissGroup(identify, new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Log.i(TAG, "onError code" + i + " msg " + s);
                            if (i == 10004 && type.equals(GroupInfo.privateGroup)) {
                                Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_quit_fail_private), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_dismiss_succ), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    mPresenter.quitGroup(identify, new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Log.i(TAG, "onError code" + i + " msg " + s);
                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_quit_succ), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                break;
            case R.id.controlOutGroup:
                intent = new Intent(this, ApplyGroupActivity.class);
                intent.putExtra("identify", identify);
                startActivity(intent);
                break;
            case R.id.member:
                intent = new Intent(this, GroupMemberActivity.class);
                intent.putExtra("id", identify);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            case R.id.addOpt:
                final String[] stringList = allowTypeContent.keySet().toArray(new String[allowTypeContent.size()]);
                new ListPickerDialog().show(stringList, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        TIMGroupManager.getInstance().modifyGroupAddOpt(identify, allowTypeContent.get(stringList[which]), new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_change_err), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess() {
                                LineControllerView opt = (LineControllerView) findViewById(R.id.addOpt);
                                opt.setContent(stringList[which]);
                            }
                        });
                    }
                });
                break;
            case R.id.nameText:
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditActivity.navToEdit(GroupProfileActivity.this, getString(R.string.chat_setting_change_group_name), info.getGroupName(), REQ_CHANGE_NAME, new EditActivity.EditInterface() {
                            @Override
                            public void onEdit(final String text, TIMCallBack callBack) {
                                TIMGroupManager.getInstance().modifyGroupName(identify, text, callBack);
                            }
                        }, 20);

                    }
                });
                break;
            case R.id.groupIntro:
                intro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditActivity.navToEdit(GroupProfileActivity.this, getString(R.string.chat_setting_change_group_intro), intro.getContent(), REQ_CHANGE_INTRO, new EditActivity.EditInterface() {
                            @Override
                            public void onEdit(final String text, TIMCallBack callBack) {
                                TIMGroupManager.getInstance().modifyGroupIntroduction(identify, text, callBack);
                            }
                        }, 20);

                    }
                });
                break;
            case R.id.messageNotify:
                final String[] messageOptList = messageOptContent.keySet().toArray(new String[messageOptContent.size()]);
                new ListPickerDialog().show(messageOptList, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        TIMGroupManager.getInstance().modifyReceiveMessageOpt(identify, messageOptContent.get(messageOptList[which]), new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_change_err), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess() {
                                LineControllerView msgNotify = (LineControllerView) findViewById(R.id.messageNotify);
                                msgNotify.setContent(messageOptList[which]);
                            }
                        });
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CHANGE_NAME) {
            if (resultCode == RESULT_OK) {
                name.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
        } else if (requestCode == REQ_CHANGE_INTRO) {
            if (resultCode == RESULT_OK) {
                intro.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
        }

    }

    private boolean isManager() {
        return roleType == TIMGroupMemberRoleType.Owner || roleType == TIMGroupMemberRoleType.Admin;
    }

    @Override
    public void setPresenter(GroupProfileContract.GroupProfilePresenter presenter) {
        mPresenter = presenter;
    }
}
