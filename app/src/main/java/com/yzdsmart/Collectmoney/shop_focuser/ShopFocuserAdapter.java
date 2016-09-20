package com.yzdsmart.Collectmoney.shop_focuser;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.ShopFocuser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/9/20.
 */

public class ShopFocuserAdapter extends RecyclerView.Adapter<ShopFocuserAdapter.ViewHolder> {
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
        if (null != shopFocusers) {
            shopFocusers.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.shop_focuser_list_item, parent, false);
        ShopFocuserAdapter.ViewHolder holder = new ShopFocuserAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopFocuser focuser = shopFocusers.get(position);
        holder.setUserAvater(focuser.getImageUrl());
        holder.setUserName(focuser.getCustPwdName());
        holder.setUserGender(focuser.getCustSex() == null ? "" : focuser.getCustSex());
    }

    @Override
    public int getItemCount() {
        return shopFocusers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.focuser_user_avater)
        CircleImageView userAvaterIV;
        @Nullable
        @BindView(R.id.focuser_user_name)
        TextView userNameTV;
        @Nullable
        @BindView(R.id.focuser_user_gender)
        TextView userGenderTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setUserAvater(String userAvater) {
            Glide.with(context).load(userAvater).error(context.getResources().getDrawable(R.mipmap.user_avater)).into(userAvaterIV);
        }

        public void setUserName(String userName) {
            userNameTV.setText(userName);
        }

        public void setUserGender(String userGender) {
            userGenderTV.setText(userGender);
        }
    }
}
