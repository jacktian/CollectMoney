package com.yzdsmart.Collectmoney.friend_future;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.FriendFuture;

import java.util.ArrayList;
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
public class FriendFutureAdapter extends RecyclerView.Adapter<FriendFutureAdapter.ViewHolder> {
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
    public void appenList(List<FriendFuture> list) {
        if (null != friendFutureList) {
            friendFutureList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != friendFutureList) {
            friendFutureList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.friendship_friendfuture_list_item, parent, false);
        FriendFutureAdapter.ViewHolder holder = new FriendFutureAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setUserNameTV(friendFutureList.get(position).getUserName());
        holder.setFriendFutureBtn(friendFutureList.get(position).getFutureType() == 0 ? true : false);
    }

    @Override
    public int getItemCount() {
        return friendFutureList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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

        }

        public void setUserNameTV(String userName) {
            userNameTV.setText(userName);
        }

        public void setFriendFutureBtn(boolean enabled) {
            friendFutureBtn.setEnabled(enabled);
        }

        @Optional
        @OnClick({R.id.friend_future_operation})
        void onClick(View view) {
            System.out.println(getPosition() + "------------------friend_future_operation---------------------" + getAdapterPosition());
        }
    }
}
