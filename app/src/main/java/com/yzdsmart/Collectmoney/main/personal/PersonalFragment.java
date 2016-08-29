package com.yzdsmart.Collectmoney.main.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.personal_friend_detail.PersonalFriendDetailActivity;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

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

    private PersonalContract.PersonalPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mPresenter.getCustLevel(SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), "000000");
        mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""));
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.to_personal_detail})
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
        userLevelIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.user_level_third));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView diamond;
        for (int i = 0; i < 3; i++) {
            diamond = new ImageView(getActivity());
            diamond.setLayoutParams(params);
            diamond.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.diamond_pink));
            diamondCountLayout.addView(diamond);
        }
    }

    @Override
    public void onGetCustInfo(String name, String headUel) {
        userNameTV.setText(name);
        Glide.with(this).load(headUel).placeholder(getActivity().getResources().getDrawable(R.mipmap.user_avater)).into(userAvaterIV);
    }
}
