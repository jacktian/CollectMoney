package com.yzdsmart.Dingdingwen.card_bag;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.BankCard;
import com.yzdsmart.Dingdingwen.utils.Utils;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BankCard bankCard = bankCardList.get(position);
        holder.setBankLogo(bankCard.getBankCode());
        holder.setCardNum(bankCard.getBankCardNum());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (0 == position) {
                    ((CardBagActivity) context).toBindBankCard();
                } else {
                    ((CardBagActivity) context).returnSelectedBankCard(bankCard);
                }
            }
        });
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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if ("ADD_BANK_CARD".equals(bankCode)) {
                params.gravity = Gravity.CENTER_HORIZONTAL;
                Glide.with(context).load(R.mipmap.add_button).asBitmap().override(Math.round(Utils.getScreenRatio(context) * 43), Math.round(Utils.getScreenRatio(context) * 43)).into(bankLogoIV);
            } else {
                params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                Glide.with(context).load("https://apimg.alipay.com/combo.png?d=cashier&t=" + bankCode).asBitmap().override(Math.round(Utils.getScreenRatio(context) * 151), Math.round(Utils.getScreenRatio(context) * 43)).into(bankLogoIV);
            }
            bankLogoIV.setLayoutParams(params);
            cardNumTV.setLayoutParams(params);
        }

        public void setCardNum(String cardNum) {
            cardNumTV.setText(Utils.cardIdHide(cardNum));
        }
    }
}
