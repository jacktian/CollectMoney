package com.yzdsmart.Collectmoney.main.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.TouristAttraction;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.views.BetterSpinner;

import java.util.ArrayList;
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
    @Nullable
    @BindView(R.id.recommend_list)
    RecyclerView recommendListRV;

    private String[] cities;
    private ArrayAdapter<String> citiesAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<TouristAttraction> touristAttractionList;
    private RecommendAdapter recommendAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cities = getResources().getStringArray(R.array.city_list);
        citiesAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.cities_list_item, cities);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        touristAttractionList = new ArrayList<TouristAttraction>();
        recommendAdapter = new RecommendAdapter(getActivity());
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

        cityRecommendSpinner.setAdapter(citiesAdapter);

        recommendListRV.setHasFixedSize(true);
        recommendListRV.setLayoutManager(mLinearLayoutManager);
        recommendListRV.setAdapter(recommendAdapter);

        List<TouristAttraction> list = new ArrayList<TouristAttraction>();
        list.add(new TouristAttraction("file:///android_asset/tourist_attractions_img.png", "武进假日酒店"));
        list.add(new TouristAttraction("file:///android_asset/tourist_attractions_img.png", "常武购物中心"));
        list.add(new TouristAttraction("file:///android_asset/tourist_attractions_img.png", "武进购物中心"));
        list.add(new TouristAttraction("file:///android_asset/tourist_attractions_img.png", "武进武悦广场"));
        touristAttractionList.addAll(list);
        recommendAdapter.appendList(touristAttractionList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
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
