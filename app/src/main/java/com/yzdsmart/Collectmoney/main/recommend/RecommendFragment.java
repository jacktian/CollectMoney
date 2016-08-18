package com.yzdsmart.Collectmoney.main.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.views.spinner.BetterSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class RecommendFragment extends BaseFragment {
    @Nullable
    @BindViews({R.id.title_left_operation_layout, R.id.left_title, R.id.center_title, R.id.title_right_operation})
    List<View> hideViews;
    @Nullable
    @BindViews({R.id.title_right_operation_to_left, R.id.bubble_count, R.id.city_recommend_spinner})
    List<View> showViews;
    @Nullable
    @BindView(R.id.city_recommend_spinner)
    BetterSpinner cityRecommendSpinner;
    @Nullable
    @BindView(R.id.title_right_operation_to_left)
    ImageView titleRightOpeTLIV;

    List<String> cityList = new LinkedList<>(Arrays.asList("常州", "无锡", "苏州"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.apply(hideViews, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
        ButterKnife.apply(showViews, ((BaseActivity) getActivity()).BUTTERKNIFEVISIBLE);
        titleRightOpeTLIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.grey_mail_icon));
//        cityRecommendSpinner.attachDataSource(cityList);
        String[] CITIES = getResources().getStringArray(R.array.city_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_list_item, CITIES);
        cityRecommendSpinner.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("------RecommendFragment------------onResume----------------------");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("------------------------------------RecommendFragment is hidden " + hidden);
    }

    @Optional
    @OnClick({R.id.title_right_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_right_operation_layout:
                ((MainActivity) getActivity()).backToFindMoney();
                break;
        }
    }
}
