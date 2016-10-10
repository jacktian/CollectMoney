package com.yzdsmart.Collectmoney.shop_details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.TIMConversationType;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.ShopScanner;
import com.yzdsmart.Collectmoney.chat.ChatActivity;
import com.yzdsmart.Collectmoney.login.LoginActivity;
import com.yzdsmart.Collectmoney.money_friendship.friend_list.add.AddFriendActivity;
import com.yzdsmart.Collectmoney.personal_friend_detail.PersonalFriendDetailActivity;
import com.yzdsmart.Collectmoney.tecent_im.bean.FriendshipInfo;
import com.yzdsmart.Collectmoney.tecent_im.bean.UserInfo;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/9/20.
 */

public class ShopScannerAdapter extends RecyclerView.Adapter<ShopScannerAdapter.ViewHolder> {
    private Context context;
    private List<ShopScanner> shopScannerList;
    private static final Integer REQUEST_LOGIN_CODE = 1000;

    public ShopScannerAdapter(Context context) {
        this.context = context;
        shopScannerList = new ArrayList<ShopScanner>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appenList(List<ShopScanner> list) {
        if (null != shopScannerList) {
            shopScannerList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != shopScannerList) {
            shopScannerList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ShopScannerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.shop_scanner_list_item, parent, false);
        ShopScannerAdapter.ViewHolder holder = new ShopScannerAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ShopScannerAdapter.ViewHolder holder, int position) {
        holder.setScannerAvater(shopScannerList.get(position).getImageUrl());
        holder.setScannerName(shopScannerList.get(position).getCustPwdName());
        holder.setCoinCounts(shopScannerList.get(position).getGoldNum());
        holder.setGetTime(shopScannerList.get(position).getTimeStr());
    }

    @Override
    public int getItemCount() {
        return shopScannerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.scanner_user_avater)
        CircleImageView scannerAvaterIV;
        @Nullable
        @BindView(R.id.scanner_user_name)
        TextView scannerNameTV;
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

        public void setScannerAvater(String followerAvater) {
            Glide.with(context).load(followerAvater).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.ic_holder_light)).error(context.getResources().getDrawable(R.mipmap.user_avater)).into(scannerAvaterIV);
        }

        public void setScannerName(String followerName) {
            scannerNameTV.setText(followerName);
        }

        public void setCoinCounts(Integer coinCounts) {
            coinCountsTV.setText("" + coinCounts);
        }

        public void setGetTime(String getTime) {
            getTimeTV.setText(getTime);
        }

        @Optional
        @OnClick({R.id.to_chat_layout})
        void onClick(View view) {
            Bundle bundle;
            switch (view.getId()) {
                case R.id.to_chat_layout:
                    if (null == SharedPreferencesUtils.getString(context, "cust_code", "") || SharedPreferencesUtils.getString(context, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                        ((BaseActivity) context).openActivityForResult(LoginActivity.class, REQUEST_LOGIN_CODE);
                        return;
                    }
                    ShopScanner shopScanner = shopScannerList.get(getLayoutPosition());
                    bundle = new Bundle();
//                    if (FriendshipInfo.getInstance().isFriend(shopScanner.getTCAccount())) {
//                        bundle.putString("identify", shopScanner.getTCAccount());
//                        bundle.putSerializable("type", TIMConversationType.C2C);
//                        ((BaseActivity) context).openActivity(ChatActivity.class, bundle, 0);
//                    } else {
//                        bundle.putString("id", shopScanner.getTCAccount());
//                        bundle.putSerializable("name", shopScanner.getTCAccount());
//                        ((BaseActivity) context).openActivity(AddFriendActivity.class, bundle, 0);
//                    }
                    bundle.putInt("type", 1);
                    bundle.putString("cust_code", shopScanner.getCustCode());
                    bundle.putString("user_code", shopScanner.getTCAccount().replace("yzd", ""));
                    ((BaseActivity) context).openActivity(PersonalFriendDetailActivity.class, bundle, 0);
                    break;
            }
        }
    }
}
