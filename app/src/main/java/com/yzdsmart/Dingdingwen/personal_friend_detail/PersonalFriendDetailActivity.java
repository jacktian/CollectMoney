package com.yzdsmart.Dingdingwen.personal_friend_detail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.TIMConversationType;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.GalleyInfo;
import com.yzdsmart.Dingdingwen.chat.ChatActivity;
import com.yzdsmart.Dingdingwen.crop.ImageCropActivity;
import com.yzdsmart.Dingdingwen.edit_personal_info.EditPersonalInfoActivity;
import com.yzdsmart.Dingdingwen.galley.GalleyActivity;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.listener.AppBarOffsetChangeListener;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.register_login.login.LoginActivity;
import com.yzdsmart.Dingdingwen.tecent_im.bean.FriendshipInfo;
import com.yzdsmart.Dingdingwen.tecent_im.bean.UserInfo;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/8/19.
 */
public class PersonalFriendDetailActivity extends BaseActivity implements PersonalFriendDetailContract.PersonalFriendDetailView {
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
    @Nullable
    @BindView(R.id.im_ope_layout)
    LinearLayout imOpeLayout;
    @Nullable
    @BindView(R.id.user_avater)
    CircleImageView userAvaterIV;
    @Nullable
    @BindView(R.id.user_gender)
    ImageView userGenderIV;
    @Nullable
    @BindView(R.id.user_remark)
    TextView userRemarkTV;
    @Nullable
    @BindView(R.id.user_nickname)
    TextView userNickNameTV;
    @Nullable
    @BindView(R.id.user_age)
    TextView userAgeTV;
    @Nullable
    @BindView(R.id.user_area)
    TextView userAreaTV;
    @Nullable
    @BindView(R.id.change_money_times)
    TextView changeMoneyTimesTV;
    @Nullable
    @BindView(R.id.coin_counts)
    TextView coinCountsTV;
    @Nullable
    @BindView(R.id.get_from_friend_counts)
    TextView getFriendCountsTV;
    @Nullable
    @BindView(R.id.galley_preview_layout)
    LinearLayout galleyPreviewLayout;
    @Nullable
    @BindView(R.id.add_friend)
    Button addFriendBtn;
    @Nullable
    @BindView(R.id.msg_chat)
    Button msgChatBtn;
    @Nullable
    @BindView(R.id.video_chat)
    Button videoChatBtn;

    private static final String TAG = "PersonalFriendDetailActivity";

    private DecimalFormat decimalFormat;

    private Integer type;//0 个人 1 好友
    private String friend_c_code;
    private String friend_identify;

    private PersonalFriendDetailContract.PersonalFriendDetailPresenter mPresenter;

    private ArrayList<GalleyInfo> galleyInfoList;

    private AlertDialog remarkDialog;

    private DateTimeFormatter dtf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        galleyInfoList = new ArrayList<GalleyInfo>();

        dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

        if (null != savedInstanceState) {
            type = savedInstanceState.getInt("type");
            friend_c_code = savedInstanceState.getString("cust_code");
            friend_identify = savedInstanceState.getString("user_code");
        } else {
            type = getIntent().getExtras().getInt("type");
            friend_c_code = getIntent().getExtras().getString("cust_code");
            friend_identify = "yzd" + getIntent().getExtras().getString("user_code");
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        switch (type) {
            case 0:
                new PersonalFriendDetailPresenter(this, this, UserInfo.getInstance().getId());
                ButterKnife.apply(imOpeLayout, BUTTERKNIFEGONE);
                break;
            case 1:
                new PersonalFriendDetailPresenter(this, this, friend_identify);
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.friend_remark_white_icon));
                if (FriendshipInfo.getInstance().isFriend(friend_identify)) {
                    ButterKnife.apply(addFriendBtn, BUTTERKNIFEGONE);
                } else {
                    ButterKnife.apply(titleRightOpeIV, BUTTERKNIFEGONE);
                    ButterKnife.apply(msgChatBtn, BUTTERKNIFEGONE);
                    ButterKnife.apply(videoChatBtn, BUTTERKNIFEGONE);
                }
                break;
        }

        if (null != friend_c_code && friend_c_code.equals(SharedPreferencesUtils.getString(this, "cust_code", ""))) {
            ButterKnife.apply(addFriendBtn, BUTTERKNIFEGONE);
        }

        MobclickAgent.openActivityDurationTrack(false);

        appbarLayout.addOnOffsetChangedListener(new AppBarOffsetChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
                    switch (type) {
                        case 0:
                            titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.menu_icon_white));
                            break;
                        case 1:
                            titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.friend_remark_white_icon));
                            break;
                    }
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
                    switch (type) {
                        case 0:
                            titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.menu_icon));
                            break;
                        case 1:
                            titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.friend_remark_icon));
                            break;
                    }
                } else {
                    //中间状态
                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_personal_friend_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        switch (type) {
            case 0:
                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                mPresenter.getCustDetailInfo("000000", "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                mPresenter.getPersonalGalley(Constants.GET_PERSONAL_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
            case 1:
                mPresenter.getCustInfo("000000", friend_c_code, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                mPresenter.getCustDetailInfo("000000", "000000", friend_c_code, SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                mPresenter.getPersonalGalley(Constants.GET_PERSONAL_GALLEY_ACTION_CODE, "000000", friend_c_code, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            if (null == MainActivity.getInstance()) return;
            MainActivity.getInstance().chatLogin();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("type", type);
        outState.putString("cust_code", friend_c_code);
        outState.putString("user_code", friend_identify);
        super.onSaveInstanceState(outState);
    }

    private Dialog deleteRemarkDialog;
    private TextView deleteFriend, remarkFriend;

    private void showDeleteRemarkDialog(Context context) {
        deleteRemarkDialog = new Dialog(context, R.style.qr_scanner_popup);
        deleteRemarkDialog.setContentView(R.layout.friend_delete_remark_choose);
        deleteFriend = (TextView) deleteRemarkDialog.findViewById(R.id.delete_friend);
        remarkFriend = (TextView) deleteRemarkDialog.findViewById(R.id.remark_friend);
        deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRemarkDialog.dismiss();
                if (!Utils.isNetUsable(PersonalFriendDetailActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.deleteFriend(friend_identify);
            }
        });
        remarkFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRemarkDialog.dismiss();
                showRemarkFriend("好友备注");
            }
        });
        Window window = deleteRemarkDialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        deleteRemarkDialog.show();
    }

    void showRemarkFriend(final String dialogTitle) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.edit_info_dialog, null);
        TextView editInfoTitle = (TextView) view.findViewById(R.id.edit_info_dialog_title);
        editInfoTitle.setText("请输入" + dialogTitle);
        final EditText remarkFriendET = (EditText) view.findViewById(R.id.edit_info_dialog_content);
        Button editCancel = (Button) view.findViewById(R.id.edit_cancel);
        Button editConfirm = (Button) view.findViewById(R.id.edit_confirm);
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkDialog.dismiss();
            }
        });
        editConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkDialog.dismiss();
                if (!Utils.isNetUsable(PersonalFriendDetailActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.remarkFriend(friend_identify, remarkFriendET.getText().toString());
            }
        });
        builder.setView(view);
        remarkDialog = builder.show();
        remarkDialog.setCancelable(false);
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.user_avater, R.id.galley_preview_layout, R.id.title_right_operation_layout, R.id.add_friend, R.id.msg_chat, R.id.video_chat})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.title_right_operation_layout:
                switch (type) {
                    case 0:
                        openActivity(EditPersonalInfoActivity.class);
                        break;
                    case 1:
                        showDeleteRemarkDialog(this);
                        break;
                }
                break;
            case R.id.user_avater:
                switch (type) {
                    case 0:
                        openActivity(ImageCropActivity.class);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.galley_preview_layout:
                bundle = new Bundle();
                bundle.putInt("identity", 0);
                bundle.putInt("type", type);
                switch (type) {
                    case 0:
                        bundle.putString("cust_code", SharedPreferencesUtils.getString(this, "cust_code", ""));
                        break;
                    case 1:
                        bundle.putString("cust_code", friend_c_code);
                        break;
                }
                openActivity(GalleyActivity.class, bundle, 0);
                break;
            case R.id.add_friend:
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.addFriend(friend_identify);
                break;
            case R.id.msg_chat:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                    return;
                }
                bundle = new Bundle();
                bundle.putString("identify", friend_identify);
                bundle.putSerializable("type", TIMConversationType.C2C);
                openActivity(ChatActivity.class, bundle, 0);
                break;
            case R.id.video_chat:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unRegisterObserver();
        super.onDestroy();
    }

    @Override
    public void onGetCustInfo(CustInfoRequestResponse response) {
        if (null != response.getNickName() && !"".equals(response.getNickName())) {
            if (response.getNickName().equals(response.getImageUrl())) {
                userNickNameTV.setText("");
            } else {
                userNickNameTV.setText(response.getNickName());
            }
        } else if (null != response.getCName() && !"".equals(response.getCName())) {
            userNickNameTV.setText(response.getCName());
        } else {
            userNickNameTV.setText(response.getC_UserCode());
        }
        userAreaTV.setText(response.getArea());
        changeMoneyTimesTV.setText("" + response.getOperNum());
        coinCountsTV.setText(decimalFormat.format(response.getGoldNum()));
        getFriendCountsTV.setText("" + response.getFriendNum());
        Glide.with(this).load(response.getImageUrl()).placeholder(getResources().getDrawable(R.mipmap.ic_holder_light)).error(getResources().getDrawable(R.mipmap.ic_holder_light)).into(userAvaterIV);
    }

    @Override
    public void onGetCustDetailInfo(CustDetailInfoRequestResponse response) {
        switch (type) {
            case 0:
                if (null != response.getCName() && !"".equals(response.getCName())) {
                    userRemarkTV.setText(response.getCName());
                } else if (null != response.getCNickName() && !"".equals(response.getCNickName())) {
                    userRemarkTV.setText(response.getCNickName());
                } else {
                    userRemarkTV.setText(response.getC_UserCode());
                }
                break;
            case 1:
                if (null != response.getCNickRemark() && !"".equals(response.getCNickRemark())) {
                    userRemarkTV.setText(response.getCNickRemark());
                } else if (null != response.getCNickName() && !"".equals(response.getCNickName())) {
                    userRemarkTV.setText(response.getCNickName());
                } else if (null != response.getCName() && !"".equals(response.getCName())) {
                    userRemarkTV.setText(response.getCName());
                } else {
                    userRemarkTV.setText(response.getC_UserCode());
                }
                break;
        }
        if (null != response.getCBirthday() && !"".equals(response.getCBirthday())) {
            DateTime birthDay = dtf.parseDateTime(response.getCBirthday());
            userAgeTV.setText((Days.daysBetween(birthDay, new DateTime()).getDays() / 365 + 1) + "岁");
        }
        if (null != response.getCSex() && !"".equals(response.getCSex()) && "女".equals(response.getCSex())) {
            userGenderIV.setImageDrawable(getResources().getDrawable(R.mipmap.gender_female_icon));
        } else {
            userGenderIV.setImageDrawable(getResources().getDrawable(R.mipmap.gender_male_icon));
        }
    }

    @Override
    public void onGetPersonalGalley(List<GalleyInfo> galleyInfos) {
        galleyInfoList.clear();
        galleyInfoList.addAll(galleyInfos);
        galleyPreviewLayout.removeAllViews();
        ImageView imageView;
        // 计算与你开发时设定的屏幕大小的纵横比
        int screenWidth = Utils.deviceWidth(this);
        int screenHeight = Utils.deviceHeight(this);
        float ratioWidth = (float) screenWidth / 480;
        float ratioHeight = (float) screenHeight / 800;
        float ratio = Math.min(ratioWidth, ratioHeight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Math.round(80 * ratio), Math.round(80 * ratio));
        for (int i = 0; i < galleyInfos.size(); i++) {
            if (i > 0) {
                params.setMargins(Math.round(2 * ratio), 0, 0, 0);
            }
            imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(this).load(galleyInfos.get(i).getImageFileUrl()).asBitmap().placeholder(getResources().getDrawable(R.mipmap.ic_holder_light)).error(getResources().getDrawable(R.mipmap.ic_holder_light)).into(imageView);
            galleyPreviewLayout.addView(imageView);
            if (i == 4) {
                return;
            }
        }
    }

    @Override
    public void refreshFriendship() {
        if (FriendshipInfo.getInstance().isFriend(friend_identify)) {
            ButterKnife.apply(addFriendBtn, BUTTERKNIFEGONE);
            ButterKnife.apply(titleRightOpeIV, BUTTERKNIFEVISIBLE);
            ButterKnife.apply(msgChatBtn, BUTTERKNIFEVISIBLE);
//            ButterKnife.apply(videoChatBtn, BUTTERKNIFEVISIBLE);
        } else {
            ButterKnife.apply(addFriendBtn, BUTTERKNIFEVISIBLE);
            ButterKnife.apply(titleRightOpeIV, BUTTERKNIFEGONE);
            ButterKnife.apply(msgChatBtn, BUTTERKNIFEGONE);
//            ButterKnife.apply(videoChatBtn, BUTTERKNIFEGONE);
        }
    }

    @Override
    public void onRemarkFriend(String remark) {
        userRemarkTV.setText(remark);
    }

    @Override
    public void setPresenter(PersonalFriendDetailContract.PersonalFriendDetailPresenter presenter) {
        mPresenter = presenter;
    }
}
