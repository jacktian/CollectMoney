package com.yzdsmart.Dingdingwen.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.meelive.ingkee.sdk.plugin.entity.UserInfo;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.GalleyInfo;
import com.yzdsmart.Dingdingwen.buy_coins.BuyCoinsActivity;
import com.yzdsmart.Dingdingwen.coupon_log.CouponLogActivity;
import com.yzdsmart.Dingdingwen.edit_personal_info.EditPersonalInfoActivity;
import com.yzdsmart.Dingdingwen.edit_shop_info.EditShopInfoActivity;
import com.yzdsmart.Dingdingwen.focused_shop.FocusedShopActivity;
import com.yzdsmart.Dingdingwen.galley.GalleyActivity;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoByPersRequestResponse;
import com.yzdsmart.Dingdingwen.payment_log.PaymentLogActivity;
import com.yzdsmart.Dingdingwen.publish_tasks.PublishTasksActivity;
import com.yzdsmart.Dingdingwen.publish_tasks_log.PublishTasksLogActivity;
import com.yzdsmart.Dingdingwen.register_business.RegisterBusinessActivity;
import com.yzdsmart.Dingdingwen.scan_coin_log.ScanCoinsLogActivity;
import com.yzdsmart.Dingdingwen.settings.SettingsActivity;
import com.yzdsmart.Dingdingwen.shop_focuser.ShopFocuserActivity;
import com.yzdsmart.Dingdingwen.shop_scanned_log.ShopScannedLogActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.photo_picker.picker.activity.BGAPhotoPickerActivity;
import com.yzdsmart.Dingdingwen.withdrawals.WithDrawActivity;
import com.yzdsmart.Dingdingwen.withdrawals_log.WithDrawLogActivity;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/10/25.
 */

public class PersonalActivity extends BaseActivity implements PersonalContract.PersonalView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.user_name)
    TextView userNameTV;
    @Nullable
    @BindView(R.id.user_avater)
    CircleImageView userAvaterIV;
    @Nullable
    @BindView(R.id.user_level)
    ImageView userLevelIV;
    @Nullable
    @BindView(R.id.diamond_count)
    LinearLayout diamondCountLayout;
    @Nullable
    @BindView(R.id.personal_coins_layout)
    LinearLayout personalCoinsLayout;
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
    @BindView(R.id.to_personal_detail)
    RelativeLayout personalDetailLayout;
    @Nullable
    @BindView(R.id.to_withdraw_log)
    RelativeLayout withdrawLogLayout;
    @Nullable
    @BindView(R.id.to_shop_withdraw_log)
    LinearLayout shopWithdrawLogLayout;
    @Nullable
    @BindView(R.id.to_shop_detail)
    LinearLayout shopDetailLayout;
    @Nullable
    @BindView(R.id.to_withdraw)
    RelativeLayout withdrawLayout;
    @Nullable
    @BindView(R.id.personal_galley_panel)
    RelativeLayout personalGalleyPanelLayout;
    @Nullable
    @BindView(R.id.to_shop_withdraw)
    LinearLayout shopWithdrawLayout;
    @Nullable
    @BindView(R.id.shop_focus_visit_panel)
    LinearLayout shopFocusVisitLayout;
    @Nullable
    @BindView(R.id.register_business_panel)
    RelativeLayout registerBusinessLayout;
    @Nullable
    @BindView(R.id.business_ope_panel)
    LinearLayout businessOpeLayout;
    @Nullable
    @BindView(R.id.user_account_coin)
    TextView userAccountCoinTV;
    @Nullable
    @BindView(R.id.shop_images_banner)
    ConvenientBanner shopImagesBanner;
    @Nullable
    @BindView(R.id.shop_name)
    TextView shopNameTV;
    @Nullable
    @BindView(R.id.shop_address)
    TextView shopAddressTV;
    @Nullable
    @BindView(R.id.focus_person_counts)
    TextView focusPersonCountsTV;
    @Nullable
    @BindView(R.id.left_total_coin_counts)
    TextView leftTotalCoinCountsTV;
    @Nullable
    @BindView(R.id.visit_person_counts)
    TextView visitPersonCountsTV;
    @Nullable
    @BindView(R.id.personal_setting_layout)
    LinearLayout personalSettingLayout;
    @Nullable
    @BindView(R.id.shop_avater)
    ImageView shopAvaterIV;

    private static final String TAG = "PersonalActivity";

    private DecimalFormat decimalFormat;

    private PersonalContract.PersonalPresenter mPresenter;

    private List<View> toggleViews;

    private ArrayList<Integer> localImages;//默认banner图片
    private ArrayList<String> galleyImages;
    private ArrayList<GalleyInfo> galleyInfoList;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!Utils.isNetUsable(PersonalActivity.this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            Bundle bundle = msg.getData();
            mPresenter.uploadShopAvater(Constants.SHOP_UPLOAD_AVATER_ACTION_CODE, SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", "") + "0.png", bundle.getString("image"), SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", ""), SharedPreferencesUtils.getString(PersonalActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(PersonalActivity.this, "ddw_access_token", ""));
        }
    };

    private TIMUserProfile timUserProfile;

    private UserInfo inKeUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText(getResources().getString(R.string.personal_find));

        mPresenter = new PersonalPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        localImages = new ArrayList<Integer>();//默认banner图片
        localImages.add(R.mipmap.shop_default_banner);
        galleyImages = new ArrayList<String>();
        galleyInfoList = new ArrayList<GalleyInfo>();

        toggleViews = new ArrayList<View>();

        toggleViews.clear();
        if (SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", "").trim().length() > 0) {
            toggleViews.add(personalDetailLayout);
            toggleViews.add(personalCoinsLayout);
            toggleViews.add(personalSettingLayout);
            toggleViews.add(personalGalleyPanelLayout);
            toggleViews.add(withdrawLayout);
            toggleViews.add(withdrawLogLayout);
        } else {
            toggleViews.add(shopImagesBanner);
            toggleViews.add(shopDetailLayout);
            toggleViews.add(shopFocusVisitLayout);
            toggleViews.add(businessOpeLayout);
            toggleViews.add(shopWithdrawLayout);
            toggleViews.add(shopWithdrawLogLayout);
        }
        ButterKnife.apply(toggleViews, BaseActivity.BUTTERKNIFEGONE);

        shopImagesBanner.setPages(new CBViewHolderCreator<ShopImageBannerHolderView>() {
            @Override
            public ShopImageBannerHolderView createHolder() {
                return new ShopImageBannerHolderView();
            }
        }, localImages);
        shopImagesBanner.setCanLoop(false);
        shopImagesBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("identity", 1);
                bundle.putInt("type", 0);
                bundle.putString("cust_code", SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", ""));
                openActivity(GalleyActivity.class, bundle, 0);
            }
        });

        inKeUserInfo = new UserInfo(SharedPreferencesUtils.getString(PersonalActivity.this, "cust_code", ""), "", 0, "");
        InKeSdkPluginAPI.login(inKeUserInfo);
        if (null != com.yzdsmart.Dingdingwen.tecent_im.bean.UserInfo.getInstance().getId() && !"".equals(com.yzdsmart.Dingdingwen.tecent_im.bean.UserInfo.getInstance().getId())) {
            //获取用户资料
            TIMFriendshipManager.getInstance().getUsersProfile(Collections.singletonList(com.yzdsmart.Dingdingwen.tecent_im.bean.UserInfo.getInstance().getId()), new TIMValueCallBack<List<TIMUserProfile>>() {
                @Override
                public void onError(int code, String desc) {
                }

                @Override
                public void onSuccess(List<TIMUserProfile> result) {
                    timUserProfile = result.get(0);
                    inKeUserInfo = new UserInfo(SharedPreferencesUtils.getString(PersonalActivity.this, "cust_code", ""), timUserProfile.getNickName(), (int) timUserProfile.getGender().getValue(), timUserProfile.getFaceUrl());
                    InKeSdkPluginAPI.updateUserInfo(inKeUserInfo);
                }
            });
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_personal;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        if (SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", "").trim().length() > 0) {
            mPresenter.getShopInfo(Constants.GET_SHOP_INFO_ACTION_CODE, "000000", SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
            mPresenter.getShopGalley(Constants.GET_SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
            shopImagesBanner.startTurning(3000);
        } else {
            mPresenter.getCustLevel(SharedPreferencesUtils.getString(PersonalActivity.this, "cust_code", ""), "000000", Constants.GET_CUST_LEVEL_ACTION_CODE, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
            mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(PersonalActivity.this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
        if (SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", "").trim().length() > 0) {
            shopImagesBanner.stopTurning();
        }
    }

    @Override
    protected void onDestroy() {
        InKeSdkPluginAPI.logout();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) return;
        if (Constants.REQUEST_CODE_CHOOSE_PHOTO == requestCode) {
            new Thread(new FormatImageRunnable(0, "" + data.getParcelableArrayListExtra("EXTRA_SELECTED_IMAGES").get(0))).start();
        }
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.user_avater, R.id.to_personal_payment_log_panel, R.id.to_personal_coupon_log_panel, R.id.to_settings, R.id.shop_avater, R.id.to_shop_settings, R.id.to_register_business, R.id.to_buy_coins, R.id.to_publish_tasks, R.id.to_personal_coins, R.id.to_publish_tasks_log, R.id.to_focused_shop, R.id.to_shop_focuser, R.id.to_withdraw_log, R.id.to_shop_withdraw_log, R.id.to_withdraw, R.id.to_shop_withdraw, R.id.to_shop_detail, R.id.to_shop_scanned_log, R.id.to_personal_galley, R.id.to_personal_qr_code})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.user_avater:
                bundle = new Bundle();
                bundle.putInt("type", 0);//0 个人 1 好友
                openActivity(EditPersonalInfoActivity.class);
                break;
            case R.id.shop_avater:
                // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
                File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "CollectMoney");
                startActivityForResult(BGAPhotoPickerActivity.newIntent(PersonalActivity.this, takePhotoDir, 1, null, true), Constants.REQUEST_CODE_CHOOSE_PHOTO);
                break;
            case R.id.to_personal_payment_log_panel:
                bundle = new Bundle();
                if (SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", "").trim().length() > 0) {
                    bundle.putInt("userType", 1);
                } else {
                    bundle.putInt("userType", 0);
                }
                openActivity(PaymentLogActivity.class, bundle, 0);
                break;
            case R.id.to_personal_coupon_log_panel:
                bundle = new Bundle();
                if (SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", "").trim().length() > 0) {
                    bundle.putInt("userType", 1);
                } else {
                    bundle.putInt("userType", 0);
                }
                openActivity(CouponLogActivity.class, bundle, 0);
                break;
            case R.id.to_settings:
            case R.id.to_shop_settings:
                openActivity(SettingsActivity.class);
                break;
            case R.id.to_register_business:
                openActivity(RegisterBusinessActivity.class);
                break;
            case R.id.to_buy_coins:
                bundle = new Bundle();
                if (SharedPreferencesUtils.getString(PersonalActivity.this, "baza_code", "").trim().length() > 0) {
                    bundle.putInt("userType", 1);
                } else {
                    bundle.putInt("userType", 0);
                }
                openActivity(BuyCoinsActivity.class, bundle, 0);
                break;
            case R.id.to_publish_tasks:
                openActivity(PublishTasksActivity.class);
                break;
            case R.id.to_personal_coins:
                openActivity(ScanCoinsLogActivity.class);
                break;
            case R.id.to_publish_tasks_log:
                openActivity(PublishTasksLogActivity.class);
                break;
            case R.id.to_focused_shop:
                openActivity(FocusedShopActivity.class);
                break;
            case R.id.to_shop_focuser:
                openActivity(ShopFocuserActivity.class);
                break;
            case R.id.to_withdraw_log:
                bundle = new Bundle();
                bundle.putInt("userType", 0);
                openActivity(WithDrawLogActivity.class, bundle, 0);
                break;
            case R.id.to_shop_withdraw_log:
                bundle = new Bundle();
                bundle.putInt("userType", 1);
                openActivity(WithDrawLogActivity.class, bundle, 0);
                break;
            case R.id.to_withdraw:
                bundle = new Bundle();
                bundle.putInt("userType", 0);
                openActivity(WithDrawActivity.class, bundle, 0);
                break;
            case R.id.to_shop_withdraw:
                bundle = new Bundle();
                bundle.putInt("userType", 1);
                openActivity(WithDrawActivity.class, bundle, 0);
                break;
            case R.id.to_shop_detail:
                openActivity(EditShopInfoActivity.class);
                break;
            case R.id.to_shop_scanned_log:
                openActivity(ShopScannedLogActivity.class);
                break;
            case R.id.to_personal_galley:
                bundle = new Bundle();
                bundle.putInt("identity", 0);
                bundle.putInt("type", 0);
                bundle.putString("cust_code", SharedPreferencesUtils.getString(PersonalActivity.this, "cust_code", ""));
                openActivity(GalleyActivity.class, bundle, 0);
                break;
            case R.id.to_personal_qr_code:
                InKeSdkPluginAPI.createLive(PersonalActivity.this, inKeUserInfo);
                break;
        }
    }

    @Override
    public void onGetCustLevel(Integer gra, Integer sta) {
        userLevelIV.setImageDrawable(null);
        diamondCountLayout.removeAllViews();
        userLevelIV.setImageDrawable(getResources().getDrawable(Utils.getMipmapId(this, "vip_orange_" + gra)));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView diamond;
        for (int i = 0; i < sta; i++) {
            diamond = new ImageView(this);
            diamond.setLayoutParams(params);
            diamond.setImageDrawable(getResources().getDrawable(R.mipmap.diamond_white));
            diamondCountLayout.addView(diamond);
        }
    }

    @Override
    public void onGetCustInfo(CustInfoRequestResponse requestResponse) {
        String name;
        if (null != requestResponse.getCName() && !"".equals(requestResponse.getCName())) {
            name = requestResponse.getCName();
        } else if (null != requestResponse.getNickName() && !"".equals(requestResponse.getNickName())) {
            name = requestResponse.getNickName();
        } else {
            name = requestResponse.getC_UserCode();
        }
        userNameTV.setText(name);
        userAccountCoinTV.setText(decimalFormat.format(requestResponse.getGoldNum()));
        changeMoneyTimesTV.setText("" + requestResponse.getOperNum());
        coinCountsTV.setText(decimalFormat.format(requestResponse.getGoldNum()));
        getFriendCountsTV.setText("" + requestResponse.getFriendNum());
        Glide.with(this).load(requestResponse.getImageUrl() == null ? "" : requestResponse.getImageUrl()).asBitmap().placeholder(getResources().getDrawable(R.mipmap.ic_holder_light)).error(getResources().getDrawable(R.mipmap.user_avater)).into(userAvaterIV);
    }

    @Override
    public void onGetShopInfo(ShopInfoByPersRequestResponse shopDetails) {
        shopNameTV.setText(shopDetails.getName());
        shopAddressTV.setText(shopDetails.getAddr());
        focusPersonCountsTV.setText("" + shopDetails.getAtteNum());
        leftTotalCoinCountsTV.setText(decimalFormat.format(shopDetails.getTotalGlodNum()));
        visitPersonCountsTV.setText("" + shopDetails.getVisiNum());
        Glide.with(this).load(shopDetails.getLogoImageUrl()).asBitmap().placeholder(getResources().getDrawable(R.mipmap.ic_holder_light)).error(getResources().getDrawable(R.mipmap.ic_holder_light)).into(shopAvaterIV);
    }

    @Override
    public void onGetShopGalley(List<GalleyInfo> galleyInfos) {
        galleyInfoList.clear();
        galleyInfoList.addAll(galleyInfos);
        galleyImages.clear();
        if (galleyInfos.size() <= 0) {
            shopImagesBanner.setPages(new CBViewHolderCreator<ShopImageBannerHolderView>() {
                @Override
                public ShopImageBannerHolderView createHolder() {
                    return new ShopImageBannerHolderView();
                }
            }, localImages);
            shopImagesBanner.stopTurning();
            shopImagesBanner.setCanLoop(false);
        } else {
            for (int i = 0; i < galleyInfos.size(); i++) {
                galleyImages.add(galleyInfos.get(i).getImageFileUrl());
                if (i == 3) {
                    break;
                }
            }
            shopImagesBanner.setPages(new CBViewHolderCreator<ShopGalleyViewHolder>() {
                @Override
                public ShopGalleyViewHolder createHolder() {
                    return new ShopGalleyViewHolder();
                }
            }, galleyImages)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
            shopImagesBanner.setCanLoop(true);
            shopImagesBanner.startTurning(3000);
        }
        shopImagesBanner.notifyDataSetChanged();
    }

    @Override
    public void onUploadShopAvater(String relaImageUrl) {
        Glide.with(this).load(relaImageUrl).asBitmap().placeholder(getResources().getDrawable(R.mipmap.ic_holder_light)).error(getResources().getDrawable(R.mipmap.ic_holder_light)).into(shopAvaterIV);
    }

    class ShopGalleyViewHolder implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.recommend_pre_load)).error(context.getResources().getDrawable(R.mipmap.bga_pp_ic_holder_light)).into(imageView);
        }
    }

    class FormatImageRunnable implements Runnable {
        private Integer index;
        private String path;

        public FormatImageRunnable(Integer index, String path) {
            this.index = index;
            this.path = path;
        }

        @Override
        public void run() {
            byte[] bytes = Utils.decodeBitmap(path);
            String image = new String(android.util.Base64.encode(bytes, android.util.Base64.DEFAULT));
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            bundle.putString("image", image);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void setPresenter(PersonalContract.PersonalPresenter presenter) {
        mPresenter = presenter;
    }
}
