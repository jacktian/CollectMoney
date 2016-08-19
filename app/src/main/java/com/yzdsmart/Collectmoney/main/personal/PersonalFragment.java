package com.yzdsmart.Collectmoney.main.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.personal_friend_detail.PersonalFriendDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/18.
 */
public class PersonalFragment extends BaseFragment {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.to_personal_detail})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                ((MainActivity) getActivity()).backToFindMoney();
                break;
            case R.id.to_personal_detail:
                ((BaseActivity) getActivity()).openActivity(PersonalFriendDetailActivity.class);
                ((MainActivity) getActivity()).backToFindMoney();
                break;
        }
    }
}
