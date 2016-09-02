package com.yzdsmart.Collectmoney.money_friendship.friend_list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.personal_friend_detail.PersonalFriendDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/8/20.
 */
public class FriendListAdapter extends UltimateViewAdapter<FriendListAdapter.ViewHolder> {
    private Context context;
    private List<Friendship> friendshipList;

    public FriendListAdapter(Context context) {
        this.context = context;
        friendshipList = new ArrayList<Friendship>();
    }

    /**
     * 添加数据
     *
     * @param list
     */
    public void appendList(List<Friendship> list) {
        if (null != friendshipList) {
            friendshipList.addAll(list);
        }
        //按首字母排序，最好从服务端获取时就排好序，减轻移动端压力
        Collections.sort(friendshipList, new Comparator<Friendship>() {
            @Override
            public int compare(Friendship user, Friendship t1) {
                try {
                    return PinyinHelper.getShortPinyin(user.getNickName()).compareTo(PinyinHelper.getShortPinyin(t1.getNickName()));
                } catch (PinyinException e) {
                    e.printStackTrace();
                }
                return -1;
            }
        });
        notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void clearList() {
        if (null != friendshipList) {
            friendshipList.clear();
        }
        notifyDataSetChanged();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.friendship_friendfuture_list_item, parent, false);
        FriendListAdapter.ViewHolder holder = new FriendListAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return friendshipList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        if (getItem(position).length() > 0)
            return getItem(position).charAt(0);
        else return -1;
    }

    public String getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < friendshipList.size())
            try {
                return PinyinHelper.getShortPinyin(friendshipList.get(position).getNickName());
            } catch (PinyinException e) {
                return "";
            }
        else return "";
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        //一定要加这个判断  因为UltimateRecyclerView本身有加了头部和尾部  这个方法返回的是包括头部和尾部在内的
//        if (position < getItemCount() && (customHeaderView != null ? position <= friendshipList.size() : position < friendshipList.size()) && (customHeaderView != null ? position > 0 : true)) {
//            position -= customHeaderView == null ? 0 : 1;
        holder.setUserAvaterIV(friendshipList.get(position).getImageUrl());
        holder.setUserNameTV(friendshipList.get(position).getNickName());
        holder.setUserLevelTV(friendshipList.get(position).getGra());
        holder.setUserDiamondCountLayout(friendshipList.get(position).getGra(), friendshipList.get(position).getSta());
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("user_code", friendshipList.get(position).getC_UserCode());
                bundle.putString("cust_code", friendshipList.get(position).getC_Code());
                ((BaseActivity) context).openActivity(PersonalFriendDetailActivity.class, bundle, 0);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.money_friendship_stick_header, parent, false);
        RecyclerView.ViewHolder holder = new FriendListAdapter.StickHeaderViewHolder(itemView) {
        };
        return holder;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ((StickHeaderViewHolder) holder).setStickHeader(("" + PinyinHelper.getShortPinyin(friendshipList.get(position).getNickName()).charAt(0)).toUpperCase());
        } catch (PinyinException e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
        @Nullable
        @BindView(R.id.friend_user_avater)
        CircleImageView userAvaterIV;
        @Nullable
        @BindView(R.id.friend_user_name)
        TextView userNameTV;
        @Nullable
        @BindView(R.id.friend_user_level)
        TextView userLevelTV;
        @Nullable
        @BindView(R.id.friend_user_diamond_count)
        LinearLayout userDiamondCountLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setUserAvaterIV(String userAvaterUrl) {
            Glide.with(context).load(userAvaterUrl).error(context.getResources().getDrawable(R.mipmap.user_avater)).into(userAvaterIV);
        }

        public void setUserNameTV(String userName) {
            userNameTV.setText(userName);
        }

        public void setUserLevelTV(Integer userLevel) {
            userLevelTV.setText("等级 : V" + userLevel);
        }

        public void setUserDiamondCountLayout(Integer userLevel, Integer diamondCounts) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ImageView diamond;
            for (int i = 0; i < diamondCounts; i++) {
                diamond = new ImageView(context);
                diamond.setLayoutParams(params);
                diamond.setImageDrawable(context.getResources().getDrawable(R.mipmap.diamond_pink));
                userDiamondCountLayout.addView(diamond);
            }
        }
    }

    class StickHeaderViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.stick_header)
        TextView stickHeaderTV;

        public StickHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setStickHeader(String stickHeader) {
            stickHeaderTV.setText(stickHeader);
        }
    }
}
