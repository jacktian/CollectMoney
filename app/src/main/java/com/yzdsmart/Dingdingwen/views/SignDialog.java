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
public class SignDialog extends Dialog {
    private Context context;
    private String signInfo;
    private Boolean signSuccess;
    private ImageView signResultIV;
    private TextView signResultTV;

    public SignDialog(Context context, String signInfo, Boolean signSuccess) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.signInfo = signInfo;
        this.signSuccess = signSuccess;
        this.setContentView(R.layout.sign_dialog);
    }

    @Override
    public void show() {
        super.show();
        signResultIV = (ImageView) findViewById(R.id.sign_result_img);
        signResultTV = (TextView) findViewById(R.id.sign_result_msg);
        Glide.with(context).load(signSuccess ? R.mipmap.sign_success : R.mipmap.sign_failed).asBitmap().placeholder(R.mipmap.ic_holder_light).error(R.mipmap.ic_holder_light).into(signResultIV);
        signResultTV.setText(signInfo);
    }
}
