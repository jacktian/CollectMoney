package com.yzdsmart.Collectmoney.main.personal;

import android.os.Bundle;
import android.os.Handler;
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
import com.bumptech.glide.Glide;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.buy_coins.BuyCoinsActivity;
import com.yzdsmart.Collectmoney.crop.ImageCropActivity;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.personal_coin_list.PersonalCoinsActivity;
import com.yzdsmart.Collectmoney.personal_friend_detail.PersonalFriendDetailActivity;
import com.yzdsmart.Collectmoney.publish_tasks.PublishTasksActivity;
import com.yzdsmart.Collectmoney.publish_tasks_log.PublishTasksLogActivity;
import com.yzdsmart.Collectmoney.register_business.RegisterBusinessActivity;
import com.yzdsmart.Collectmoney.settings.SettingsActivity;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
import com.yzdsmart.Collectmoney.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/8/18.
 */
public class PersonalFragment extends BaseFragment implements PersonalContract.PersonalView {
    @Nullable
    @BindViews({R.id.center_title, R.id.title_logo, R.id.title_right_operation})
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
    @BindView(R.id.left_title)
    TextView leftTitleTV;
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
    @BindView(R.id.to_personal_detail)
    RelativeLayout personalDetailLayout;
    @Nullable
    @BindView(R.id.to_shop_detail)
    RelativeLayout shopDetailLayout;
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
    @BindView(R.id.daily_coin_counts)
    TextView dailyCoinCountsTV;
    @Nullable
    @BindView(R.id.visit_person_counts)
    TextView visitPersonCountsTV;
    @Nullable
    @BindView(R.id.shop_level)
    LinearLayout shopLevelLayout;

    private PersonalContract.PersonalPresenter mPresenter;

    private List<View> toggleViews;

    private Handler backFindMoneyHandler = new Handler();
    private Runnable backFindMoneyRunnable;

    private ArrayList<Integer> localImages = new ArrayList<Integer>();//banner图片

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toggleViews = new ArrayList<View>();

        backFindMoneyRunnable = new Runnable() {
            @Override
            public void run() {
                ((MainActivity) getActivity()).backToFindMoney();
            }
        };

        new PersonalPresenter(getActivity(), this);

        localImages.add(R.mipmap.shop_banner);
        localImages.add(R.mipmap.shop_banner);
        localImages.add(R.mipmap.shop_banner);
        localImages.add(R.mipmap.shop_banner);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_personal;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.apply(hideViews, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.left_arrow));
        titleRightOpeTLIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.grey_mail_icon));
        leftTitleTV.setText(getActivity().getResources().getString(R.string.personal_find));

        toggleViews.clear();
        if (SharedPreferencesUtils.getString(getActivity(), "baza_code", "").trim().length() > 0) {
            toggleViews.add(personalDetailLayout);
            toggleViews.add(registerBusinessLayout);

            mPresenter.getShopInfo("000000", "000000", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""), SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));

            shopImagesBanner.setPages(new CBViewHolderCreator<ShopImageBannerHolderView>() {
                @Override
                public ShopImageBannerHolderView createHolder() {
                    return new ShopImageBannerHolderView();
                }
            }, localImages)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
            shopImagesBanner.startTurning(3000);
        } else {
            toggleViews.add(shopImagesBanner);
            toggleViews.add(shopDetailLayout);
            toggleViews.add(shopFocusVisitLayout);
            toggleViews.add(businessOpeLayout);

            mPresenter.getCustLevel(SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), "000000");
            mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
        }
        ButterKnife.apply(toggleViews, BaseActivity.BUTTERKNIFEGONE);

    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.to_personal_detail, R.id.user_avater, R.id.to_settings, R.id.to_register_business, R.id.to_buy_coins, R.id.to_publish_tasks, R.id.to_personal_coins, R.id.to_publish_tasks_log})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                ((MainActivity) getActivity()).backToFindMoney();
                break;
            case R.id.to_personal_detail:
                bundle = new Bundle();
                bundle.putInt("type", 0);//0 个人 1 好友
                ((BaseActivity) getActivity()).openActivity(PersonalFriendDetailActivity.class, bundle, 0);
//                backFindMoneyHandler.postDelayed(backFindMoneyRunnable, 1500);
                break;
            case R.id.user_avater:
                ((BaseActivity) getActivity()).openActivity(ImageCropActivity.class);
                break;
            case R.id.to_settings:
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
                ((BaseActivity) getActivity()).openActivity(PersonalCoinsActivity.class);
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
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (SharedPreferencesUtils.getString(getActivity(), "baza_code", "").trim().length() > 0) {
//                mPresenter.getShopInfo("000000", "000000", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""), SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));

                shopImagesBanner.startTurning(3000);
            } else {
//                mPresenter.getCustLevel(SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), "000000");
//                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
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
//                mPresenter.getShopInfo("000000", "000000", SharedPreferencesUtils.getString(getActivity(), "baza_code", ""), SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));

                shopImagesBanner.startTurning(3000);
            } else {
//                mPresenter.getCustLevel(SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), "000000");
//                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
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
    public void setPresenter(PersonalContract.PersonalPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetCustLevel(Integer gra, Integer sta) {
        userLevelIV.setImageDrawable(null);
        diamondCountLayout.removeAllViews();
        userLevelIV.setImageDrawable(getActivity().getResources().getDrawable(Utils.getMipmapId(getActivity(), "vip" + gra)));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView diamond;
        for (int i = 0; i < sta; i++) {
            diamond = new ImageView(getActivity());
            diamond.setLayoutParams(params);
            diamond.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.diamond_pink));
            diamondCountLayout.addView(diamond);
        }
    }

    @Override
    public void onGetCustInfo(String name, String headUel, Integer goldNum) {
        userNameTV.setText(name);
        userAccountCoinTV.setText("" + goldNum);
        Glide.with(this).load(headUel).error(getActivity().getResources().getDrawable(R.mipmap.user_avater)).into(userAvaterIV);
    }

    @Override
    public void onGetShopInfo(ShopInfoRequestResponse shopDetails) {
        shopNameTV.setText(shopDetails.getName());
        shopAddressTV.setText(shopDetails.getAddr());
        focusPersonCountsTV.setText("" + shopDetails.getAtteNum());
        dailyCoinCountsTV.setText("" + shopDetails.getTodayGlodNum());
        visitPersonCountsTV.setText("" + shopDetails.getVisiNum());
        shopLevelLayout.removeAllViews();
        ImageView shopLevel = new ImageView(getActivity());
        shopLevel.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.five_start));
        shopLevelLayout.addView(shopLevel);
    }
}
