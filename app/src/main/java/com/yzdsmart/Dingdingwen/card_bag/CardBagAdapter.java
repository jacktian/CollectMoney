package com.yzdsmart.Dingdingwen.card_bag;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.BankCard;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/12/13.
 */

public class CardBagAdapter extends RecyclerView.Adapter<CardBagAdapter.ViewHolder> {
    private Context context;
    private List<BankCard> bankCardList;

    public CardBagAdapter(Context context) {
        this.context = context;
        bankCardList = new ArrayList<BankCard>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<BankCard> list) {
        if (null != bankCardList) {
            bankCardList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != bankCardList && bankCardList.size() > 0) {
            bankCardList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.bank_card_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BankCard bankCard = bankCardList.get(position);
        if ("ADD_BANK_CARD".equals(bankCard.getBankCode())) {
            holder.setBankLogo(bankCard.getBankCode());
        }
    }

    @Override
    public int getItemCount() {
        return bankCardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.bank_logo)
        ImageView bankLogoIV;
        @Nullable
        @BindView(R.id.card_num)
        TextView cardNumTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setBankLogo(String bankCode) {
            if ("ADD_BANK_CARD".equals(bankCode)) {
                Glide.with(context).load(R.mipmap.add_bank_card).asBitmap().into(bankLogoIV);
            }
        }

        public void setCardNum(String cardNum) {
            cardNumTV.setText(cardNum);
        }
    }
}
