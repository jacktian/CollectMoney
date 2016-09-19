package com.yzdsmart.Collectmoney.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.R;


/**
 * Created by YZD on 2016/5/20.
 */
public class CustomDialog extends Dialog {
    private Context context;
    private int drawableResourceId;
    private String dialogText;
    private ImageView loadingIV;
    private TextView loadingTV;
    private AnimationDrawable loadingDrawable;

    public CustomDialog(Context context, int resourceId, String dialogText) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.drawableResourceId = resourceId;
        this.dialogText = dialogText;
        this.setContentView(R.layout.common_dialog);
    }

    @Override
    public void show() {
        super.show();
        loadingIV = (ImageView) findViewById(R.id.loading);
        loadingTV = (TextView) findViewById(R.id.loading_text);
        loadingIV.setImageResource(drawableResourceId);
        loadingDrawable = (AnimationDrawable) loadingIV.getDrawable();
        loadingDrawable.start();
        loadingTV.setText(dialogText);
    }

    @Override
    public void dismiss() {
        loadingDrawable.stop();
        super.dismiss();
    }
}
