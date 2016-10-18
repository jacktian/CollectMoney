package com.yzdsmart.Collectmoney.friend_future;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.tencent.TIMFriendAddResponse;
import com.tencent.TIMFriendResponseType;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMFutureFriendType;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.bean.FriendFuture;

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
 * Created by YZD on 2016/8/21.
 */
public class FriendFutureAdapter extends UltimateViewAdapter<FriendFutureAdapter.ViewHolder> {
    private Context context;
    private List<FriendFuture> friendFutureList;

    public FriendFutureAdapter(Context context) {
        this.context = context;
        friendFutureList = new ArrayList<FriendFuture>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<FriendFuture> list) {
        if (null != friendFutureList) {
            friendFutureList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != friendFutureList && friendFutureList.size() > 0) {
            friendFutureList.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.friendship_friendfuture_list_item, parent, false);
        FriendFutureAdapter.ViewHolder holder = new FriendFutureAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return friendFutureList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FriendFuture future = friendFutureList.get(position);
        holder.setUserNameTV(future.getName());
        //获取用户资料
        TIMFriendshipManager.getInstance().getUsersProfile(Collections.singletonList(future.getIdentify()), new TIMValueCallBack<List<TIMUserProfile>>() {
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
        holder.setFriendFutureBtn(future.getType());
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
        @BindViews({R.id.friend_future_operation})
        List<View> showViews;
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
                userAvaterIV.setImageDrawable(context.getResources().getDrawable(R.mipmap.tecent_head_other));
                return;
            }
            Glide.with(context).load(userAvaterUrl).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.ic_holder_light)).error(context.getResources().getDrawable(R.mipmap.tecent_head_other)).into(userAvaterIV);
        }

        public void setUserNameTV(String userName) {
            userNameTV.setText(userName);
        }

        public void setFriendFutureBtn(TIMFutureFriendType type) {
            switch (type) {
                case TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE:
                    friendFutureBtn.setEnabled(true);
                    friendFutureBtn.setText(context.getResources().getString(R.string.accept_friend));
                    break;
                case TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE:
                    friendFutureBtn.setEnabled(false);
                    friendFutureBtn.setText(context.getResources().getString(R.string.wait_accept_friend));
                    break;
                case TIM_FUTURE_FRIEND_DECIDE_TYPE:
                    friendFutureBtn.setEnabled(false);
                    friendFutureBtn.setText(context.getResources().getString(R.string.already_accept_friend));
                    break;
            }
        }

        @Optional
        @OnClick({R.id.friend_future_operation})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.friend_future_operation:
                    final FriendFuture future = friendFutureList.get(getAdapterPosition());
                    TIMFriendAddResponse response = new TIMFriendAddResponse();
                    response.setIdentifier(future.getIdentify());
                    response.setType(TIMFriendResponseType.AgreeAndAdd);
                    TIMFriendshipManager.getInstance().addFriendResponse(response, new TIMValueCallBack<TIMFriendResult>() {
                        @Override
                        public void onError(int i, String s) {
                        }

                        @Override
                        public void onSuccess(TIMFriendResult timFriendResult) {
                            future.setType(TIMFutureFriendType.TIM_FUTURE_FRIEND_DECIDE_TYPE);
                            notifyDataSetChanged();
                        }
                    });
                    break;
            }
        }
    }
}
