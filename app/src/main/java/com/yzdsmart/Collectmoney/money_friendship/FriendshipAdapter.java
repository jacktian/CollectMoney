package com.yzdsmart.Collectmoney.money_friendship;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/8/20.
 */
public class FriendshipAdapter extends UltimateViewAdapter<FriendshipAdapter.ViewHolder>{
    private Context context;
    private List<Friendship> friendshipList;

    public FriendshipAdapter(Context context) {
        this.context = context;
        friendshipList=new ArrayList<Friendship>();
    }

    /**
     * 添加数据
     * @param list
     */
    public void appendList(List<Friendship> list){
        if(null!=friendshipList){
            friendshipList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void clearList(){
        if(null!=friendshipList){
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
        View itemView= LayoutInflater.from(context).inflate(R.layout.friendship_list_item,parent,false);
        FriendshipAdapter.ViewHolder holder=new FriendshipAdapter.ViewHolder(itemView);
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
        //一定要加这个判断  因为UltimateRecyclerView本身有加了头部和尾部  这个方法返回的是包括头部和尾部在内的
        if (position < getItemCount() && (customHeaderView != null ? position <= friendshipList.size() : position < friendshipList.size()) && (customHeaderView != null ? position > 0 : true)) {
            position  -= customHeaderView==null?0:1;
            holder.setUserNameTV(friendshipList.get(position).getUserName());
            holder.setUserLevelTV(friendshipList.get(position).getUserLevel());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class  ViewHolder extends UltimateRecyclerviewViewHolder{
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
        LinearLayout userDiamondCountLL;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setUserAvaterIV(String userAvaterUrl) {

        }

        public void setUserNameTV(String userName) {
            userNameTV.setText(userName);
        }

        public void setUserLevelTV(Integer userLevel) {
           userLevelTV.setText("等级 : V"+userLevel);
        }

        public void setUserDiamondCountLL(Integer userLevel,Integer diamondCounts) {

        }
    }
}
