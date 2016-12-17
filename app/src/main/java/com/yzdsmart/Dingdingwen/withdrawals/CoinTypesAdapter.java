package com.yzdsmart.Dingdingwen.withdrawals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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

public class CoinTypesAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<CoinType> coinTypes;
    private CoinType coinType;

    private ArrayList<CoinType> mOriginalValues;
    private final Object mLock = new Object();
    private MyFilter myFilter;

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
        Glide.with(context).load((null == coinType.getLogoLink() || "".equals(coinType.getLogoLink())) ? R.mipmap.yzd_coin : coinType.getLogoLink()).asBitmap().placeholder(R.mipmap.ic_holder_light).error(R.mipmap.ic_holder_light).into(holder.coinLogoIV);
        holder.coinNameTV.setText(coinTypes.get(i).getGoldName());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (null == myFilter) {
            myFilter = new MyFilter();
        }
        return myFilter;
    }

    class ViewHolder {
        ImageView coinLogoIV;
        TextView coinNameTV;
    }

    class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
            FilterResults results = new FilterResults();
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    // 将list的用户 集合转换给这个原始数据的ArrayList
                    mOriginalValues = new ArrayList<CoinType>(coinTypes);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<CoinType> list = new ArrayList<CoinType>(
                            mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                // 声明一个临时的集合对象 将原始数据赋给这个临时变量
                final ArrayList<CoinType> values = mOriginalValues;
                final int count = values.size();
                // 新的集合对象
                final ArrayList<CoinType> newValues = new ArrayList<CoinType>(count);
                // 然后将这个新的集合数据赋给FilterResults对象
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            // 重新将与适配器相关联的List重赋值一下
            coinTypes = (List<CoinType>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
