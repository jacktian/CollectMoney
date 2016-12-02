package com.yzdsmart.Dingdingwen.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.BackgroundBag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/12/2.
 */

public class BackgroundBagAdapter extends UltimateViewAdapter<BackgroundBagAdapter.ViewHolder> {
    private Context context;
    private List<BackgroundBag> bagList;

    public BackgroundBagAdapter(Context context) {
        this.context = context;
        bagList = new ArrayList<BackgroundBag>();
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendList(List<BackgroundBag> list) {
        if (null != bagList) {
            bagList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearList() {
        if (null != bagList && bagList.size() > 0) {
            bagList.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.background_bg_adapter, parent, false);
        BackgroundBagAdapter.ViewHolder holder = new BackgroundBagAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return bagList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BackgroundBag backgroundBag = bagList.get(position);
        holder.setBagType(backgroundBag.getType());
        holder.setBagCounts(backgroundBag.getCounts());
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
        @BindView(R.id.bag_type)
        ImageView bagTypeIV;
        @Nullable
        @BindView(R.id.bag_counts)
        TextView bagCountsTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setBagType(Integer bagType) {
            bagTypeIV.setImageDrawable(bagType == 0 ? context.getResources().getDrawable(R.mipmap.background_bag_coin) : context.getResources().getDrawable(R.mipmap.background_bag_diamond));
        }

        public void setBagCounts(Integer bagCounts) {
            bagCountsTV.setText(bagCounts + "个");
        }
    }
}
