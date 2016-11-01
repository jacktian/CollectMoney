package com.yzdsmart.Dingdingwen.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.yzdsmart.Dingdingwen.R;

/**
 * Created by YZD on 2016/9/12.
 */
public class StaticDialog extends Dialog {
    private Context context;
    private Bitmap bitmap;
    private ImageView getCoinIV;

    public StaticDialog(Context context, Bitmap bitmap) {
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
