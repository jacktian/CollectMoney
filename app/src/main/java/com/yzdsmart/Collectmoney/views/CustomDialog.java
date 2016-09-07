package com.yzdsmart.Collectmoney.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.yzdsmart.Collectmoney.R;


/**
 * Created by YZD on 2016/5/20.
 */
public class CustomDialog extends Dialog {
    private Context context;
    private int drawableResourceId;
    private ImageView loadingIV;
    private AnimationDrawable loadingDrawable;

    public CustomDialog(Context context, int resourceId) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.drawableResourceId = resourceId;
        this.setContentView(R.layout.common_dialog);
    }

    @Override
    public void show() {
        super.show();
        loadingIV = (ImageView) findViewById(R.id.loading);
        loadingIV.setImageResource(drawableResourceId);
        loadingDrawable = (AnimationDrawable) loadingIV.getDrawable();
        loadingDrawable.start();
    }

    @Override
    public void dismiss() {
        loadingDrawable.stop();
        super.dismiss();
    }
}
