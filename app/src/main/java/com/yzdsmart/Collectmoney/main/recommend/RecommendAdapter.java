package com.yzdsmart.Collectmoney.main.recommend;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.TouristAttraction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/8/19.
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private Context context;
    private List<TouristAttraction> touristAttractionList;

    public RecommendAdapter(Context context) {
        this.context = context;
        touristAttractionList = new ArrayList<TouristAttraction>();
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendList(List<TouristAttraction> list) {
        if (null != touristAttractionList) {
            touristAttractionList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearList() {
        if (null != touristAttractionList) {
            touristAttractionList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recommend_list_item, parent, false);
        RecommendAdapter.ViewHolder holder = new RecommendAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setAttractionImg(touristAttractionList.get(position).getAttractionImgUrl());
        holder.setAttractionName(touristAttractionList.get(position).getAttractionName());
    }

    @Override
    public int getItemCount() {
        return touristAttractionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
            Glide.with(context).load(attractionImg).into(attractionImgIV);
        }

        public void setAttractionName(String attractionName) {
            attractionNameTV.setText(attractionName);
        }
    }
}
