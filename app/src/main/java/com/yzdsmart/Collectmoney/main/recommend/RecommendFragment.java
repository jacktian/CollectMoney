package com.yzdsmart.Collectmoney.main.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.response.ExpandListRequestResponse;
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
public class RecommendFragment extends BaseFragment implements RecommendContract.RecommendView {
    @Nullable
    @BindViews({R.id.title_left_operation_layout, R.id.left_title, R.id.title_logo, R.id.title_right_operation})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindViews({R.id.city_recommend_spinner})
    List<View> showViews;
    @Nullable
    @BindView(R.id.city_recommend_spinner)
    BetterSpinner cityRecommendSpinner;
    //    @Nullable
//    @BindView(R.id.title_right_operation_to_left)
//    ImageView titleRightOpeTLIV;
    @Nullable
    @BindView(R.id.recommend_list)
    UltimateRecyclerView recommendListRV;
//    @Nullable
//    @BindView(R.id.bubble_count)
//    TextView bubbleCountTV;

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;
    private RecommendContract.RecommendPresenter mPresenter;

    private String[] cities;
    private ArrayAdapter<String> citiesAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<ExpandListRequestResponse> expandList;
    private RecommendAdapter recommendAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RecommendPresenter(getActivity(), this);

        cities = getResources().getStringArray(R.array.city_list);
        citiesAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.cities_list_item, cities);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        expandList = new ArrayList<ExpandListRequestResponse>();
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
        centerTitleTV.setText(getActivity().getResources().getString(R.string.recommend));
//        titleRightOpeTLIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.grey_mail_icon));

        cityRecommendSpinner.setAdapter(citiesAdapter);
        cityRecommendSpinner.setText(cities[0]);

        recommendListRV.setHasFixedSize(true);
        recommendListRV.setLayoutManager(mLinearLayoutManager);
        recommendListRV.setAdapter(recommendAdapter);
        recommendListRV.reenableLoadmore();
        recommendListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                mPresenter.getExpandList("000000", pageIndex, PAGE_SIZE);
            }
        });
        recommendListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recommendListRV.setRefreshing(false);
                recommendListRV.reenableLoadmore();
                pageIndex = 1;
                recommendAdapter.clearList();
                mPresenter.getExpandList("000000", pageIndex, PAGE_SIZE);
            }
        });

        mPresenter.getExpandList("000000", pageIndex, PAGE_SIZE);
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

    @Override
    public void setPresenter(RecommendContract.RecommendPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetExpandList(List<ExpandListRequestResponse> expands) {
//        bubbleCountTV.setText("" + expands.size());
        pageIndex++;
        expandList.clear();
        expandList.addAll(expands);
        recommendAdapter.appendList(expandList);
        if (expands.size() < PAGE_SIZE) {
            recommendListRV.disableLoadmore();
        }
    }
}
