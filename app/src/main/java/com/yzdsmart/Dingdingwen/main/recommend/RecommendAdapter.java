package com.yzdsmart.Dingdingwen.main.recommend;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.response.ExpandListRequestResponse;
import com.yzdsmart.Collectmoney.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/19.
 */
public class RecommendAdapter extends UltimateViewAdapter<RecommendAdapter.ViewHolder> {
    private Context context;
    private List<ExpandListRequestResponse> expandList;

    public RecommendAdapter(Context context) {
        this.context = context;
        expandList = new ArrayList<ExpandListRequestResponse>();
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendList(List<ExpandListRequestResponse> list) {
        if (null != expandList) {
            expandList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearList() {
        if (null != expandList && expandList.size() > 0) {
            expandList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recommend_expand_list_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return expandList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setAttractionImg(expandList.get(position).getImageUrl());
        holder.setAttractionName(expandList.get(position).getName());
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
        @Nullable
        @BindView(R.id.recommend_item_image)
        ImageView attractionImgIV;
        @Nullable
        @BindView(R.id.recommend_item_name)
        TextView attractionNameTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setAttractionImg(String attractionImg) {
            Glide.with(context).load(attractionImg).error(context.getResources().getDrawable(R.mipmap.recommend_load_error)).into(attractionImgIV);

//            ViewTreeObserver vto = attractionImgIV.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    attractionImgIV.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    attractionImgIV.getHeight();
//                    attractionImgIV.getWidth();
//                    System.out.println(attractionImgIV.getHeight() + "----------------" + attractionImgIV.getWidth());
//                }
//            });
        }

        public void setAttractionName(String attractionName) {
            attractionNameTV.setText(attractionName);
        }

        @Optional
        @OnClick({R.id.recommend_item})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.recommend_item:
                    ((MainActivity) context).backToFindMoney();
                    ((MainActivity) context).getShopListNearByMarket(expandList.get(getAdapterPosition()).getName(), expandList.get(getAdapterPosition()).getCoor());
                    break;
            }
        }
    }
}
