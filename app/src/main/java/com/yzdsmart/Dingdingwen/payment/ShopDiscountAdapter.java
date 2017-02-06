package com.yzdsmart.Dingdingwen.payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.ShopDiscount;
import com.yzdsmart.Dingdingwen.utils.DoubleUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2016/12/18.
 */

public class ShopDiscountAdapter extends BaseAdapter {
    private Context context;
    private List<ShopDiscount> shopDiscounts;
    private ShopDiscount shopDiscount;
    private DecimalFormat decimalFormat;

    public ShopDiscountAdapter(Context context) {
        this.context = context;
        shopDiscounts = new ArrayList<ShopDiscount>();
        decimalFormat = new DecimalFormat("#0");
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<ShopDiscount> list) {
        if (null != list) {
            shopDiscounts.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != shopDiscounts && shopDiscounts.size() > 0) {
            shopDiscounts.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return shopDiscounts.size();
    }

    @Override
    public Object getItem(int i) {
        return shopDiscounts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.shop_discount_item,
                    null);
            holder = new ViewHolder();
            holder.discountContentTV = (TextView) view.findViewById(R.id.discount_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        shopDiscount = shopDiscounts.get(i);
        switch (shopDiscount.getDisType()) {
            case 23:
                holder.discountContentTV.setText(decimalFormat.format(100d * shopDiscount.getDiscReta()) + " 折");
                break;
            case 45:
                holder.discountContentTV.setText("满 " + shopDiscount.getFullPrice() + " 减 " + shopDiscount.getDiscPrice());
                break;
        }
        return view;
    }

    class ViewHolder {
        TextView discountContentTV;
    }
}
