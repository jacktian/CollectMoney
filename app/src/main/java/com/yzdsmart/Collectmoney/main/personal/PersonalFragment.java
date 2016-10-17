package com.yzdsmart.Collectmoney.main.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.GalleyInfo;
import com.yzdsmart.Collectmoney.buy_coins.BuyCoinsActivity;
import com.yzdsmart.Collectmoney.edit_personal_info.EditPersonalInfoActivity;
import com.yzdsmart.Collectmoney.edit_shop_info.EditShopInfoActivity;
import com.yzdsmart.Collectmoney.focused_shop.FocusedShopActivity;
import com.yzdsmart.Collectmoney.galley.preview.GalleyPreviewActivity;
import com.yzdsmart.Collectmoney.galley.upload.UploadGalleyActivity;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopInfoByPersRequestResponse;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.publish_tasks.PublishTasksActivity;
import com.yzdsmart.Collectmoney.publish_tasks_log.PublishTasksLogActivity;
import com.yzdsmart.Collectmoney.register_business.RegisterBusinessActivity;
import com.yzdsmart.Collectmoney.scan_coin_log.ScanCoinsLogActivity;
import com.yzdsmart.Collectmoney.settings.SettingsActivity;
import com.yzdsmart.Collectmoney.shop_focuser.ShopFocuserActivity;
import com.yzdsmart.Collectmoney.shop_scanned_log.ShopScannedLogActivity;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
import com.yzdsmart.Collectmoney.utils.Utils;
import com.yzdsmart.Collectmoney.withdrawals.WithDrawActivity;
import com.yzdsmart.Collectmoney.withdrawals_log.WithDrawLogActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/8/18.
 */
public class PersonalFragment extends BaseFragment implements PersonalContract.PersonalView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation_to_left)
    ImageView titleRightOpeTLIV;
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

    private static final String SHOP_INFO_ACTION_CODE = "3666";
    private static final String GET_SHOP_GALLEY_ACTION_CODE = "5101";
    private static final String SHOP_UPLOAD_AVATER_ACTION_CODE = "5001";//上传商铺相册
    private static final String GET_CUST_LEVEL_ACTION_CODE = "612";

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;

    private PersonalContract.PersonalPresenter mPresenter;

    private List<View> toggleViews;

    private Handler backFindMoneyHandler = new Handler();
    private Runnable backFindMoneyRunnable;

    private ArrayList<Integer> localImages;//默认banner图片
    private ArrayList<String> galleyImages;
    private ArrayList<GalleyInfo> galleyInfoList;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            mPresenter.uploadShopAvater(SHOP_UPLOAD_AVATER_ACTION_CODE, SharedPreferencesUtils.getString(getActivity(), "baza_code", "") + "0.png", bundle.getString("image"), SharedPreferencesUtils.getString(getActivity(), "baza_code", ""));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localImages = new ArrayList<Integer>();//默认banner图片
        localImages.add(R.mipmap.shop_banner);
        galleyImages = new ArrayList<String>();
        galleyInfoList = new ArrayList<GalleyInfo>();

        toggleViews = new ArrayList<View>();

        backFindMoneyRunnable = new Runnable() {
            @Override
            public void run() {
                ((MainActivity) getActivity()).backToFindMoney();
            }
        };

        new PersonalPresenter(getActivity(), this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_personal;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.apply(hideViews, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.left_arrow_white));
        titleRightOpeTLIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.grey_mail_icon));
        centerTitleTV.setText(getActivity().getResources().getString(R.string.personal_find));

        toggleViews.clear();
        if (SharedPreferencesUtils.getString(getActivity(), "baza_code", "").trim().length() > 0) {
            toggleViews.add(personalDetailLayout);
            toggleViews.add(personalCoinsLayout);
            toggleViews.add(personalSettingLayout);
            toggleViews.add(personalGalleyPanelLayout);
            toggleViews.add(withdrawLayout);
            toggleViews.add(withdrawLogLayout);

//            mPresenter.getShopInfo(SHOP_INFO_ACTION_CODE, "000000", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""), SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
//            mPresenter.getShopGalley(GET_SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""));
        } else {
            toggleViews.add(shopImagesBanner);
            toggleViews.add(shopDetailLayout);
            toggleViews.add(shopFocusVisitLayout);
            toggleViews.add(businessOpeLayout);
            toggleViews.add(shopWithdrawLayout);
            toggleViews.add(shopWithdrawLogLayout);

//            mPresenter.getCustLevel(SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), "000000");
//            mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
        }
        ButterKnife.apply(toggleViews, BaseActivity.BUTTERKNIFEGONE);

        shopImagesBanner.setPages(new CBViewHolderCreator<ShopImageBannerHolderView>() {
            @Override
            public ShopImageBannerHolderView createHolder() {
                return new ShopImageBannerHolderView();
            }
        }, localImages);

        shopImagesBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("identity", 1);
                bundle.putString("cust_code", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""));
                bundle.putParcelableArrayList("galleys", galleyInfoList);
                ((BaseActivity) getActivity()).openActivity(GalleyPreviewActivity.class, bundle, 0);
            }
        });
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.user_avater, R.id.to_settings, R.id.shop_avater, R.id.to_shop_settings, R.id.to_register_business, R.id.to_buy_coins, R.id.to_publish_tasks, R.id.to_personal_coins, R.id.to_publish_tasks_log, R.id.to_focused_shop, R.id.to_shop_focuser, R.id.to_withdraw_log, R.id.to_shop_withdraw_log, R.id.to_withdraw, R.id.to_shop_withdraw, R.id.to_shop_detail, R.id.to_shop_scanned_log, R.id.to_personal_galley})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                ((MainActivity) getActivity()).backToFindMoney();
                break;
            case R.id.user_avater:
                bundle = new Bundle();
                bundle.putInt("type", 0);//0 个人 1 好友
                ((BaseActivity) getActivity()).openActivity(EditPersonalInfoActivity.class);
                break;
            case R.id.to_personal_detail:
//                bundle = new Bundle();
//                bundle.putInt("type", 0);//0 个人 1 好友
//                ((BaseActivity) getActivity()).openActivity(PersonalFriendDetailActivity.class, bundle, 0);
//                ((BaseActivity) getActivity()).openActivity(EditPersonalInfoActivity.class);
//                backFindMoneyHandler.postDelayed(backFindMoneyRunnable, 1500);
                break;
            case R.id.shop_avater:
                // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
                File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "CollectMoney");
                startActivityForResult(BGAPhotoPickerActivity.newIntent(getActivity(), takePhotoDir, 1, null, true), REQUEST_CODE_CHOOSE_PHOTO);
                break;
            case R.id.to_settings:
            case R.id.to_shop_settings:
                ((BaseActivity) getActivity()).openActivity(SettingsActivity.class);
                break;
            case R.id.to_register_business:
                ((BaseActivity) getActivity()).openActivity(RegisterBusinessActivity.class);
//                backFindMoneyHandler.postDelayed(backFindMoneyRunnable, 1500);
                break;
            case R.id.to_buy_coins:
                ((BaseActivity) getActivity()).openActivity(BuyCoinsActivity.class);
//                backFindMoneyHandler.postDelayed(backFindMoneyRunnable, 1500);
                break;
            case R.id.to_publish_tasks:
                ((BaseActivity) getActivity()).openActivity(PublishTasksActivity.class);
//                backFindMoneyHandler.postDelayed(backFindMoneyRunnable, 1500);
                break;
            case R.id.to_personal_coins:
                ((BaseActivity) getActivity()).openActivity(ScanCoinsLogActivity.class);
//                backFindMoneyHandler.postDelayed(backFindMoneyRunnable, 1500);
                break;
//            case R.id.to_money_friend:
//                ((BaseActivity) getActivity()).openActivity(MoneyFriendshipActivity.class);
//                backFindMoneyHandler.postDelayed(backFindMoneyRunnable, 1500);
//                break;
            case R.id.to_publish_tasks_log:
                ((BaseActivity) getActivity()).openActivity(PublishTasksLogActivity.class);
//                backFindMoneyHandler.postDelayed(backFindMoneyRunnable, 1500);
                break;
            case R.id.to_focused_shop:
                ((BaseActivity) getActivity()).openActivity(FocusedShopActivity.class);
                break;
            case R.id.to_shop_focuser:
                ((BaseActivity) getActivity()).openActivity(ShopFocuserActivity.class);
                break;
            case R.id.to_withdraw_log:
                bundle = new Bundle();
                bundle.putInt("userType", 0);
                ((BaseActivity) getActivity()).openActivity(WithDrawLogActivity.class, bundle, 0);
                break;
            case R.id.to_shop_withdraw_log:
                bundle = new Bundle();
                bundle.putInt("userType", 1);
                ((BaseActivity) getActivity()).openActivity(WithDrawLogActivity.class, bundle, 0);
                break;
            case R.id.to_withdraw:
                bundle = new Bundle();
                bundle.putInt("userType", 0);
                ((BaseActivity) getActivity()).openActivity(WithDrawActivity.class, bundle, 0);
                break;
            case R.id.to_shop_withdraw:
                bundle = new Bundle();
                bundle.putInt("userType", 1);
                ((BaseActivity) getActivity()).openActivity(WithDrawActivity.class, bundle, 0);
                break;
            case R.id.to_shop_detail:
                ((BaseActivity) getActivity()).openActivity(EditShopInfoActivity.class);
                break;
            case R.id.to_shop_scanned_log:
                ((BaseActivity) getActivity()).openActivity(ShopScannedLogActivity.class);
                break;
            case R.id.to_personal_galley:
                bundle = new Bundle();
                bundle.putInt("identity", 0);
                bundle.putInt("type", 0);
                bundle.putString("cust_code", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
                ((BaseActivity) getActivity()).openActivity(GalleyPreviewActivity.class, bundle, 0);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (SharedPreferencesUtils.getString(getActivity(), "baza_code", "").trim().length() > 0) {
                mPresenter.getShopInfo(SHOP_INFO_ACTION_CODE, "000000", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""));
                mPresenter.getShopGalley(GET_SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""));
                shopImagesBanner.startTurning(3000);
            } else {
                mPresenter.getCustLevel(SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), "000000", GET_CUST_LEVEL_ACTION_CODE);
                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
            }
        } else {
            mPresenter.unRegisterSubscribe();
            if (SharedPreferencesUtils.getString(getActivity(), "baza_code", "").trim().length() > 0) {
                shopImagesBanner.stopTurning();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.isVisible()) {
            if (SharedPreferencesUtils.getString(getActivity(), "baza_code", "").trim().length() > 0) {
                mPresenter.getShopInfo(SHOP_INFO_ACTION_CODE, "000000", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""));
                mPresenter.getShopGalley(GET_SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""));
                shopImagesBanner.startTurning(3000);
            } else {
                mPresenter.getCustLevel(SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), "000000", GET_CUST_LEVEL_ACTION_CODE);
                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
        if (SharedPreferencesUtils.getString(getActivity(), "baza_code", "").trim().length() > 0) {
            shopImagesBanner.stopTurning();
        }
    }

    @Override
    public void onDestroy() {
        backFindMoneyHandler.removeCallbacks(backFindMoneyRunnable);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) return;
        if (REQUEST_CODE_CHOOSE_PHOTO == requestCode) {
            new Thread(new FormatImageRunnable(0, "" + data.getParcelableArrayListExtra("EXTRA_SELECTED_IMAGES").get(0))).start();
        }
    }

    @Override
    public void setPresenter(PersonalContract.PersonalPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetCustLevel(Integer gra, Integer sta) {
        userLevelIV.setImageDrawable(null);
        diamondCountLayout.removeAllViews();
        userLevelIV.setImageDrawable(getActivity().getResources().getDrawable(Utils.getMipmapId(getActivity(), "vip_orange_" + gra)));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView diamond;
        for (int i = 0; i < sta; i++) {
            diamond = new ImageView(getActivity());
            diamond.setLayoutParams(params);
            diamond.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.diamond_white));
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
        userAccountCoinTV.setText(" " + requestResponse.getGoldNum());
        changeMoneyTimesTV.setText("" + requestResponse.getOperNum());
        coinCountsTV.setText("" + requestResponse.getGoldNum());
        getFriendCountsTV.setText("" + requestResponse.getFriendNum());
        Glide.with(this).load(requestResponse.getImageUrl() == null ? "" : requestResponse.getImageUrl()).asBitmap().placeholder(getActivity().getResources().getDrawable(R.mipmap.ic_holder_light)).error(getActivity().getResources().getDrawable(R.mipmap.user_avater)).into(userAvaterIV);
    }

    @Override
    public void onGetShopInfo(ShopInfoByPersRequestResponse shopDetails) {
        shopNameTV.setText(shopDetails.getName());
        shopAddressTV.setText(shopDetails.getAddr());
        focusPersonCountsTV.setText("" + shopDetails.getAtteNum());
        leftTotalCoinCountsTV.setText("" + shopDetails.getTotalGlodNum());
        visitPersonCountsTV.setText("" + shopDetails.getVisiNum());
        Glide.with(this).load(shopDetails.getLogoImageUrl()).asBitmap().placeholder(getActivity().getResources().getDrawable(R.mipmap.ic_holder_light)).error(getActivity().getResources().getDrawable(R.mipmap.ic_holder_light)).into(shopAvaterIV);
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
            shopImagesBanner.startTurning(3000);
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
            shopImagesBanner.startTurning(3000);
        }
        shopImagesBanner.notifyDataSetChanged();
    }

    @Override
    public void onUploadShopAvater(String relaImageUrl) {
        Glide.with(this).load(relaImageUrl).asBitmap().placeholder(getActivity().getResources().getDrawable(R.mipmap.ic_holder_light)).error(getActivity().getResources().getDrawable(R.mipmap.ic_holder_light)).into(shopAvaterIV);
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
            Glide.with(context).load(data).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.recommend_pre_load)).error(context.getResources().getDrawable(R.mipmap.shop_banner)).into(imageView);
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
}
