package com.yzdsmart.Collectmoney.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.yzdsmart.Collectmoney.R;

/**
 * Created by YZD on 2016/9/12.
 */
public class GetCoinDialog extends Dialog {
    private Context context;
    private Bitmap bitmap;
    private ImageView getCoinIV;

    public GetCoinDialog(Context context, Bitmap bitmap) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.bitmap = bitmap;
        this.setContentView(R.layout.common_dialog);
    }

    @Override
    public void show() {
        super.show();
        getCoinIV = (ImageView) findViewById(R.id.loading);
        getCoinIV.setImageBitmap(bitmap);
    }
}
