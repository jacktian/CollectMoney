package com.yzdsmart.Collectmoney.money_friendship.recommend_friends;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.tencent.TIMConversationType;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.chat.ChatActivity;
import com.yzdsmart.Collectmoney.money_friendship.friend_list.add.AddFriendActivity;
import com.yzdsmart.Collectmoney.personal_friend_detail.PersonalFriendDetailActivity;
import com.yzdsmart.Collectmoney.tecent_im.bean.FriendshipInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/9/25.
 */

public class RecommendFriendsAdapter extends UltimateViewAdapter<RecommendFriendsAdapter.ViewHolder> {
    private Context context;
    private List<Friendship> friendshipList;

    public RecommendFriendsAdapter(Context context) {
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
        RecommendFriendsAdapter.ViewHolder holder = new RecommendFriendsAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return friendshipList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Friendship friendship = friendshipList.get(position);
        holder.setUserAvater(friendship.getImageUrl());
        holder.setUserName(friendship.getNickName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
//                if (FriendshipInfo.getInstance().isFriend(friendship.getTCAccount())) {
//                    bundle.putString("identify", friendship.getTCAccount());
//                    bundle.putSerializable("type", TIMConversationType.C2C);
//                    ((BaseActivity) context).openActivity(ChatActivity.class, bundle, 0);
//                } else {
//                    bundle.putString("id", friendship.getTCAccount());
//                    bundle.putString("name", friendship.getTCAccount());
//                    ((BaseActivity) context).openActivity(AddFriendActivity.class, bundle, 0);
//                }
                bundle.putInt("type", 1);
                bundle.putString("cust_code", friendship.getC_Code());
                bundle.putString("user_code", friendship.getC_UserCode());
                ((BaseActivity) context).openActivity(PersonalFriendDetailActivity.class, bundle, 0);
            }
        });
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
        @BindViews({R.id.friend_user_level, R.id.friend_user_diamond_count})
        List<View> hideViews;
        @Nullable
        @BindView(R.id.friend_user_avater)
        CircleImageView userAvaterIV;
        @Nullable
        @BindView(R.id.friend_user_name)
        TextView userNameTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ButterKnife.apply(hideViews, BaseActivity.BUTTERKNIFEGONE);
        }

        public void setUserAvater(String userAvater) {
            Glide.with(context).load(userAvater).error(context.getResources().getDrawable(R.mipmap.user_avater)).into(userAvaterIV);
        }

        public void setUserName(String userName) {
            userNameTV.setText(userName);
        }
    }
}
