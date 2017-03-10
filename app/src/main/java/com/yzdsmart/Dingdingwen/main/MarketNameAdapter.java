package com.yzdsmart.Dingdingwen.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2017/3/10.
 */

public class MarketNameAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<String> marketNames;

    public MarketNameAdapter(Context context) {
        this.context = context;
        marketNames = new ArrayList<String>();
    }

    public void appendList(List<String> list) {
        if (null != marketNames) {
            marketNames.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return marketNames.size();
    }

    @Override
    public Object getItem(int i) {
        return marketNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.market_name_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.marketNameTV = (TextView) convertView.findViewById(R.id.tv_tinted_spinner);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.marketNameTV.setText(marketNames.get(i));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new MyFilter();
    }

    class ViewHolder {
        private TextView marketNameTV;
    }

    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new MyFilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }

        class MyFilterResults extends Filter.FilterResults {

            public MyFilterResults() {
                values = marketNames;
                count = marketNames.size();
            }
        }
    }
}
