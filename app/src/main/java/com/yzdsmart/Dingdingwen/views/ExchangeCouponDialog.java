package com.yzdsmart.Dingdingwen.views;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.CouponBean;

/**
 * Created by YZD on 2016/9/12.
 */
public class ExchangeCouponDialog extends Dialog {
    private Context context;
    private CouponBean couponBean;
    private ImageView coinLogoIV;
    private TextView couponContentTV;

    public ExchangeCouponDialog(Context context, CouponBean couponBean) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.couponBean = couponBean;
        this.setContentView(R.layout.exchange_coupon_dialog);
    }

    @Override
    public void show() {
        super.show();
        coinLogoIV = (ImageView) findViewById(R.id.coin_logo);
        couponContentTV = (TextView) findViewById(R.id.coupon_content);
        Glide.with(context).load((null == couponBean.getLogoLink() || "".equals(couponBean.getLogoLink())) ? R.mipmap.yzd_coin : couponBean.getLogoLink()).asBitmap().placeholder(R.mipmap.ic_holder_light).error(R.mipmap.ic_holder_light).into(coinLogoIV);
        couponContentTV.setText("使用 " + couponBean.getGoldNum() + " 个 " + couponBean.getGoldName() + " 换取\n" + couponBean.getShow());
    }
}
