package com.yzdsmart.Collectmoney.money_friendship.recommend_friends;

import android.content.Context;
import android.os.Bundle;
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
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.personal_friend_detail.PersonalFriendDetailActivity;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/9/25.
 */

public class RecommendFriendsAdapter extends UltimateViewAdapter<RecommendFriendsAdapter.ViewHolder> {
    private Context context;
    private List<Friendship> friendshipList;
    private DateTimeFormatter dtf;

    public RecommendFriendsAdapter(Context context) {
        this.context = context;
        friendshipList = new ArrayList<Friendship>();
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
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
        if (null != friendshipList && friendshipList.size() > 0) {
            friendshipList.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.recommend_friend_list_item, parent, false);
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
        if (!"".equals(friendship.getCBirthday())) {
            DateTime birthDay = dtf.parseDateTime(friendship.getCBirthday());
            holder.setUserAge("(" + (Days.daysBetween(birthDay, new DateTime()).getDays() / 365 + 1) + "岁)");
        }
        holder.setUserGender(friendship.getCSex());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
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
        @BindView(R.id.recommend_user_avater)
        CircleImageView userAvaterIV;
        @Nullable
        @BindView(R.id.recommend_user_name)
        TextView userNameTV;
        @Nullable
        @BindView(R.id.recommend_user_age)
        TextView userAgeTV;
        @Nullable
        @BindView(R.id.recommend_user_gender)
        ImageView userGenderIV;
        @Nullable
        @BindView(R.id.recommend_user_signature)
        TextView userSignatureTV;

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

        public void setUserAge(String userAge) {
            userAgeTV.setText(userAge);
        }

        public void setUserGender(String userGender) {
            userGenderIV.setImageDrawable(userGender.equals("男") ? context.getResources().getDrawable(R.mipmap.gender_male_icon) : context.getResources().getDrawable(R.mipmap.gender_female_icon));
        }

        public void setUserSignature(String userTCAccount) {
//            //获取用户资料
//            TIMFriendshipManager.getInstance().getUsersProfile(Collections.singletonList(userTCAccount), new TIMValueCallBack<List<TIMUserProfile>>() {
//                @Override
//                public void onError(int code, String desc) {
//                    //错误码code和错误描述desc，可用于定位请求失败原因
//                    //错误码code列表请参见错误码表
//                }
//
//                @Override
//                public void onSuccess(List<TIMUserProfile> result) {
//                    for (TIMUserProfile res : result) {
//                        userSignatureTV.setText("".equals(res.getSelfSignature()) ? "用户个性签名" : res.getSelfSignature());
//                    }
//                }
//            });
        }
    }
}
