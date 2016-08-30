package com.yzdsmart.Collectmoney.money_friendship.conversation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.bean.Conversation;
import com.yzdsmart.Collectmoney.tecent_im.utils.TimeUtil;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/8/30.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private Context context;
    private List<Conversation> conversationList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ConversationAdapter(Context context) {
        this.context = context;
        conversationList = new LinkedList<Conversation>();
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendList(List<Conversation> list) {
        if (null != conversationList) {
            conversationList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearList() {
        if (null != conversationList) {
            conversationList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.tecent_item_conversation, parent, false);
        ConversationAdapter.ViewHolder holder = new ConversationAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 如果设置了回调，则设置点击事件
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
        Conversation data = conversationList.get(position);
        holder.tvName.setText(data.getName());
        holder.avatar.setImageResource(data.getAvatar());
        holder.lastMessage.setText(data.getLastMessageSummary());
        holder.time.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        long unRead = data.getUnreadNum();
        if (unRead <= 0) {
            holder.unread.setVisibility(View.INVISIBLE);
        } else {
            holder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10) {
                holder.unread.setBackgroundResource(R.mipmap.tecent_point1);
            } else {
                holder.unread.setBackgroundResource(R.mipmap.tecent_point2);
                if (unRead > 99) {
                    unReadStr = context.getResources().getString(R.string.time_more);
                }
            }
            holder.unread.setText(unReadStr);
        }
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.name)
        public TextView tvName;
        @Nullable
        @BindView(R.id.avatar)
        public CircleImageView avatar;
        @Nullable
        @BindView(R.id.last_message)
        public TextView lastMessage;
        @Nullable
        @BindView(R.id.message_time)
        public TextView time;
        @Nullable
        @BindView(R.id.unread_num)
        public TextView unread;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
