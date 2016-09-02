package com.yzdsmart.Collectmoney.money_friendship.conversation;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.chat.ChatActivity;
import com.yzdsmart.Collectmoney.tecent_im.adapters.ConversationAdapter;
import com.yzdsmart.Collectmoney.tecent_im.bean.Conversation;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jacks on 2016/8/29.
 */
public class ConversationFragment extends BaseFragment {
    @Nullable
    @BindView(R.id.conversation_list)
    RecyclerView conversationListRV;

    private List<Conversation> conversationList;
    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private ConversationAdapter conversationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationList = new LinkedList<Conversation>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        conversationAdapter = new ConversationAdapter(getActivity());
        conversationAdapter.setOnItemClickListener(new ConversationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Conversation conversation = conversationList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("identify", conversation.getIdentify());
                bundle.putSerializable("type", conversation.getType());
                ((BaseActivity) getActivity()).openActivity(ChatActivity.class, bundle, 0);
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_conversation;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(getActivity()).paint(dividerPaint).build();
        conversationListRV.setHasFixedSize(true);
        conversationListRV.setLayoutManager(mLinearLayoutManager);
        conversationListRV.addItemDecoration(dividerItemDecoration);
        conversationListRV.setAdapter(conversationAdapter);
    }

    /**
     * 更新会话列表
     *
     * @param conversationList
     */
    public void updateConversions(List<Conversation> conversationList) {
        this.conversationList.clear();
        this.conversationList.addAll(conversationList);

        conversationAdapter.clearList();
        conversationAdapter.appendList(this.conversationList);
    }
}
