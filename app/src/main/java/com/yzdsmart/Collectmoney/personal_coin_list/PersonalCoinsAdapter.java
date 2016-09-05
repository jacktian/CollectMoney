package com.yzdsmart.Collectmoney.personal_coin_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yzdsmart.Collectmoney.bean.GetCoinsLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/9/5.
 */
public class PersonalCoinsAdapter extends RecyclerView.Adapter<PersonalCoinsAdapter.ViewHolder> {
    private Context context;
    private List<GetCoinsLog> logList;

    public PersonalCoinsAdapter(Context context) {
        this.context = context;
        logList = new ArrayList<GetCoinsLog>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<GetCoinsLog> list) {
        if (null != logList) {
            logList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != logList) {
            logList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
