package com.yzdsmart.Dingdingwen.shop_focuser;

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
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.ShopFocuser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/9/20.
 */

public class ShopFocuserAdapter extends UltimateViewAdapter<ShopFocuserAdapter.ViewHolder> {
    private Context context;
    private List<ShopFocuser> shopFocusers;

    public ShopFocuserAdapter(Context context) {
        this.context = context;
        shopFocusers = new ArrayList<ShopFocuser>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<ShopFocuser> list) {
        if (null != shopFocusers) {
            shopFocusers.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != shopFocusers && shopFocusers.size() > 0) {
            shopFocusers.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.shop_focuser_list_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return shopFocusers.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopFocuser focuser = shopFocusers.get(position);
        holder.setUserAvater(focuser.getImageUrl());
        holder.setUserName(focuser.getCustPwdName());
        holder.setUserGender(focuser.getCustSex() == null ? "" : focuser.getCustSex());
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
        @BindView(R.id.focuser_user_avater)
        CircleImageView userAvaterIV;
        @Nullable
        @BindView(R.id.focuser_user_name)
        TextView userNameTV;
        @Nullable
        @BindView(R.id.focuser_user_gender)
        ImageView userGenderTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setUserAvater(String userAvater) {
            Glide.with(context).load(userAvater).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.ic_holder_light)).error(context.getResources().getDrawable(R.mipmap.user_avater)).into(userAvaterIV);
        }

        public void setUserName(String userName) {
            userNameTV.setText(userName);
        }

        public void setUserGender(String userGender) {
            userGenderTV.setImageDrawable("男".equals(userGender) ? context.getResources().getDrawable(R.mipmap.gender_male_icon) : context.getResources().getDrawable(R.mipmap.gender_female_icon));
        }
    }
}
