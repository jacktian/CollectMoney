package com.yzdsmart.Collectmoney.shop_details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.ShopFollower;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/8/22.
 */
public class ShopFollowerAdapter extends RecyclerView.Adapter<ShopFollowerAdapter.ViewHolder> {
    private Context context;
    private List<ShopFollower> hotelFollowerList;

    public ShopFollowerAdapter(Context context) {
        this.context = context;
        hotelFollowerList = new ArrayList<ShopFollower>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appenList(List<ShopFollower> list) {
        if (null != hotelFollowerList) {
            hotelFollowerList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != hotelFollowerList) {
            hotelFollowerList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.hotel_follower_list_item, parent, false);
        ShopFollowerAdapter.ViewHolder holder = new ShopFollowerAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setFollowerName(hotelFollowerList.get(position).getUserName());
        holder.setCoinCounts(hotelFollowerList.get(position).getCoins());
        holder.setGetTime(hotelFollowerList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return hotelFollowerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.follower_user_avater)
        CircleImageView followerAvaterIV;
        @Nullable
        @BindView(R.id.follower_user_name)
        TextView followerNameTV;
        @Nullable
        @BindView(R.id.get_coin_counts)
        TextView coinCountsTV;
        @Nullable
        @BindView(R.id.get_time)
        TextView getTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setFollowerAvater(String followerAvater) {
        }

        public void setFollowerName(String followerName) {
            followerNameTV.setText(followerName);
        }

        public void setCoinCounts(Integer coinCounts) {
            coinCountsTV.setText("" + coinCounts);
        }

        public void setGetTime(String getTime) {
            getTimeTV.setText(getTime);
        }
    }
}
