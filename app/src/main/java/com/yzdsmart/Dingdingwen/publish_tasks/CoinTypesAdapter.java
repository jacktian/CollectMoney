package com.yzdsmart.Dingdingwen.publish_tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.CoinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2016/12/17.
 */

public class CoinTypesAdapter extends BaseAdapter {
    private Context context;
    private List<CoinType> coinTypes;
    private CoinType coinType;

    public CoinTypesAdapter(Context context) {
        this.context = context;
        coinTypes = new ArrayList<CoinType>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<CoinType> list) {
        if (null != list) {
            coinTypes.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != coinTypes && coinTypes.size() > 0) {
            coinTypes.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return coinTypes.size();
    }

    @Override
    public Object getItem(int i) {
        return coinTypes.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.coin_type_item,
                    null);
            holder = new ViewHolder();
            holder.coinLogoIV = (ImageView) view.findViewById(R.id.coin_logo);
            holder.coinNameTV = (TextView) view.findViewById(R.id.coin_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        coinType = coinTypes.get(i);
        Glide.with(context).load((null == coinType.getLogoLink() || "".equals(coinType.getLogoLink())) ? R.mipmap.yzd_coin : coinType.getLogoLink()).asBitmap().placeholder(R.mipmap.ic_holder_light).error(R.mipmap.coin_logo_fail_default).into(holder.coinLogoIV);
        holder.coinNameTV.setText(coinType.getGoldName());
        return view;
    }

    class ViewHolder {
        ImageView coinLogoIV;
        TextView coinNameTV;
    }
}
