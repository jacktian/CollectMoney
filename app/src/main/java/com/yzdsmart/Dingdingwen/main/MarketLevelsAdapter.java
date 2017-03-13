package com.yzdsmart.Dingdingwen.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jacks on 2017/3/13.
 */

public class MarketLevelsAdapter extends RecyclerView.Adapter<MarketLevelsAdapter.ViewHolder> {
    private Context context;
    private List<String> marketLevels;

    public MarketLevelsAdapter(Context context) {
        this.context = context;
        marketLevels = new ArrayList<String>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<String> list) {
        if (null != marketLevels) {
            marketLevels.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != marketLevels && marketLevels.size() > 0) {
            marketLevels.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.market_levels_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String marketLevel = marketLevels.get(position);
        holder.setMarketLevel(marketLevel);
    }

    @Override
    public int getItemCount() {
        return marketLevels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.market_level)
        TextView marketLevelTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setMarketLevel(String marketLevel) {
            marketLevelTV.setText(marketLevel);
        }
    }
}
