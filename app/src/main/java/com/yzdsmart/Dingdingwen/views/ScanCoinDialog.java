package com.yzdsmart.Dingdingwen.views;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Dingdingwen.R;

/**
 * Created by YZD on 2016/9/12.
 */
public class ScanCoinDialog extends Dialog {
    private Context context;
    private String coinLogo;
    private Double coinCounts;
    private ImageView coinTypeIV;
    private TextView coinCountsTV;

    public ScanCoinDialog(Context context, String coinLogo, Double coinCounts) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.coinLogo = coinLogo;
        this.coinCounts = coinCounts;
        this.setContentView(R.layout.scan_coin_dialog);
    }

    @Override
    public void show() {
        super.show();
        coinTypeIV = (ImageView) findViewById(R.id.coin_type);
        coinCountsTV = (TextView) findViewById(R.id.coin_counts);
        Glide.with(context).load("".equals(coinLogo) ? R.mipmap.yzd_coin : coinLogo).asBitmap().placeholder(R.mipmap.ic_holder_light).error(R.mipmap.ic_holder_light).into(coinTypeIV);
        coinCountsTV.setText(coinCounts + "个");
    }
}
