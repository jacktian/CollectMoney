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
    private Integer coinType;
    private String coinLogo;
    private Integer coinCounts;
    private ImageView coinTypeIV;
    private TextView coinCountsTV;

    public ScanCoinDialog(Context context, Integer coinType, String coinLogo, Integer coinCounts) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.coinType = coinType;
        this.coinLogo = coinLogo;
        this.coinCounts = coinCounts;
        this.setContentView(R.layout.scan_coin_dialog);
    }

    @Override
    public void show() {
        super.show();
        coinTypeIV = (ImageView) findViewById(R.id.coin_type);
        coinCountsTV = (TextView) findViewById(R.id.coin_counts);
        Glide.with(context).load(0 == coinType ? R.mipmap.yzd_coin : coinLogo).asBitmap().into(coinTypeIV);
        coinCountsTV.setText(coinCounts + "个");
    }
}
