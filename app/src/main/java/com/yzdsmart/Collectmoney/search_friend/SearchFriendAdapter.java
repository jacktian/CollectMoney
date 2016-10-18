package com.yzdsmart.Collectmoney.search_friend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.TIMConversationType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMFriendshipProxy;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.chat.ChatActivity;
import com.yzdsmart.Collectmoney.tecent_im.bean.ProfileSummary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/9/2.
 */
public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.ViewHolder> {
    private Context context;
    private List<ProfileSummary> profileSummaryList;
    private ProfileSummary summary;
    private boolean isFriend = false;

    public SearchFriendAdapter(Context context) {
        this.context = context;
        profileSummaryList = new ArrayList<ProfileSummary>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<ProfileSummary> list) {
        if (null != profileSummaryList) {
            profileSummaryList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != profileSummaryList && profileSummaryList.size() > 0) {
            profileSummaryList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.friendship_friendfuture_list_item, parent, false);
        SearchFriendAdapter.ViewHolder holder = new SearchFriendAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        isFriend = false;
        summary = profileSummaryList.get(position);
        holder.setUserNameTV(summary.getName());
        //获取用户资料
        TIMFriendshipManager.getInstance().getUsersProfile(Collections.singletonList(summary.getIdentify()), new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                holder.setUserAvaterIV(null);
            }

            @Override
            public void onSuccess(List<TIMUserProfile> result) {
                for (TIMUserProfile res : result) {
                    holder.setUserAvaterIV(res.getFaceUrl());
                }
            }
        });
        List<TIMUserProfile> profiles = TIMFriendshipProxy.getInstance().getFriends();
        for (TIMUserProfile profile : profiles) {
            if (summary.getIdentify().equals(profile.getIdentifier())) {
                isFriend = true;
                break;
            }
        }
        if (isFriend) {
            holder.setFriendFutureBtn(false);
        } else {
            holder.setFriendFutureBtn(true);
        }
    }

    @Override
    public int getItemCount() {
        return profileSummaryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindViews({R.id.friend_user_level, R.id.friend_user_diamond_count})
        List<View> hideViews;
        @Nullable
        @BindViews({R.id.friend_future_operation})
        List<View> showViews;
        @Nullable
        @BindView(R.id.friend_item_layout)
        RelativeLayout friendItemLayout;
        @Nullable
        @BindView(R.id.friend_user_avater)
        CircleImageView userAvaterIV;
        @Nullable
        @BindView(R.id.friend_user_name)
        TextView userNameTV;
        @Nullable
        @BindView(R.id.friend_future_operation)
        Button friendFutureBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ButterKnife.apply(hideViews, BaseActivity.BUTTERKNIFEGONE);
            ButterKnife.apply(showViews, BaseActivity.BUTTERKNIFEVISIBLE);
        }

        public void setUserAvaterIV(String userAvaterUrl) {
            if (null == userAvaterUrl) {
                Glide.with(context).load(context.getResources().getDrawable(R.mipmap.tecent_head_other)).into(userAvaterIV);
                return;
            }
            Glide.with(context).load(userAvaterUrl).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.ic_holder_light)).error(context.getResources().getDrawable(R.mipmap.tecent_head_other)).into(userAvaterIV);
        }

        public void setUserNameTV(String userName) {
            userNameTV.setText(userName);
        }

        public void setFriendFutureBtn(boolean show) {
            friendFutureBtn.setText(context.getResources().getString(R.string.add_friend));
            friendFutureBtn.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        @Optional
        @OnClick({R.id.friend_future_operation, R.id.friend_item_layout})
        void onClick(final View view) {
            switch (view.getId()) {
                case R.id.friend_future_operation:
                    ((SearchFriendActivity) context).applyAddFriend(summary.getIdentify());
                    break;
                case R.id.friend_item_layout:
                    if (isFriend) {
                        Bundle bundle = new Bundle();
                        bundle.putString("identify", summary.getIdentify());
                        bundle.putSerializable("type", TIMConversationType.C2C);
                        ((BaseActivity) context).openActivity(ChatActivity.class, bundle, 0);
                    }
                    break;
            }
        }
    }
}
