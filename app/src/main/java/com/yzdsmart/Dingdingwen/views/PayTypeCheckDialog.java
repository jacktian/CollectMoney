package com.yzdsmart.Dingdingwen.views;

import android.app.Dialog;
import android.content.Context;

import com.yzdsmart.Dingdingwen.R;

/**
 * Created by YZD on 2016/9/12.
 */
public class PayTypeCheckDialog extends Dialog {

    public PayTypeCheckDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.setContentView(R.layout.pay_type_check_dialog);
    }
}
