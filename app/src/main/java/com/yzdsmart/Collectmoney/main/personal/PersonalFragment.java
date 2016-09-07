package com.yzdsmart.Collectmoney.main.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.buy_coins.BuyCoinsActivity;
import com.yzdsmart.Collectmoney.crop.ImageCropActivity;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.money_friendship.MoneyFriendshipActivity;
import com.yzdsmart.Collectmoney.personal_coin_list.PersonalCoinsActivity;
import com.yzdsmart.Collectmoney.personal_friend_detail.PersonalFriendDetailActivity;
import com.yzdsmart.Collectmoney.publish_tasks.PublishTasksActivity;
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
    @BindViews({R.id.title_right_operation_to_left, R.id.bubble_count})
    List<View> showViews;
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
    @BindView(R.id.register_business_layout)
    RelativeLayout registerBusinessLayout;
    @Nullable
    @BindView(R.id.buy_coins_layout)
    RelativeLayout buyCoinsLayout;
    @Nullable
    @BindView(R.id.focus_shop_layout)
    RelativeLayout focusShopLayout;
    @Nullable
    @BindView(R.id.publish_tasks_layout)
    RelativeLayout publishTasksLayout;
    @Nullable
    @BindView(R.id.user_account_coin)
    TextView userAccountCoinTV;

    private PersonalContract.PersonalPresenter mPresenter;

    private List<View> toggleViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toggleViews = new ArrayList<View>();

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
        ButterKnife.apply(showViews, ((BaseActivity) getActivity()).BUTTERKNIFEVISIBLE);
        titleLeftOpeIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.left_arrow));
        titleRightOpeTLIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.grey_mail_icon));
        leftTitleTV.setText(getActivity().getResources().getString(R.string.personal_find));

        toggleViews.clear();
        if (SharedPreferencesUtils.getString(getActivity(), "baza_code", "").trim().length() > 0) {
            toggleViews.add(registerBusinessLayout);
            toggleViews.add(focusShopLayout);
        } else {
            toggleViews.add(buyCoinsLayout);
            toggleViews.add(publishTasksLayout);
        }
        ButterKnife.apply(toggleViews, BaseActivity.BUTTERKNIFEGONE);

        mPresenter.getCustLevel(SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), "000000");
        mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.to_personal_detail, R.id.user_avater, R.id.to_settings, R.id.to_register_business, R.id.to_buy_coins, R.id.to_publish_tasks, R.id.to_personal_coins, R.id.to_money_friend})
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
                ((MainActivity) getActivity()).backToFindMoney();
                break;
            case R.id.user_avater:
                ((BaseActivity) getActivity()).openActivity(ImageCropActivity.class);
                break;
            case R.id.to_settings:
                ((BaseActivity) getActivity()).openActivity(SettingsActivity.class);
                break;
            case R.id.to_register_business:
                ((BaseActivity) getActivity()).openActivity(RegisterBusinessActivity.class);
                ((MainActivity) getActivity()).backToFindMoney();
                break;
            case R.id.to_buy_coins:
                ((BaseActivity) getActivity()).openActivity(BuyCoinsActivity.class);
                ((MainActivity) getActivity()).backToFindMoney();
                break;
            case R.id.to_publish_tasks:
                ((BaseActivity) getActivity()).openActivity(PublishTasksActivity.class);
                ((MainActivity) getActivity()).backToFindMoney();
                break;
            case R.id.to_personal_coins:
                ((BaseActivity) getActivity()).openActivity(PersonalCoinsActivity.class);
                ((MainActivity) getActivity()).backToFindMoney();
                break;
            case R.id.to_money_friend:
                ((BaseActivity) getActivity()).openActivity(MoneyFriendshipActivity.class);
                ((MainActivity) getActivity()).backToFindMoney();
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.getCustLevel(SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), "000000");
            mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
        } else {
            mPresenter.unRegisterSubscribe();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
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
}
