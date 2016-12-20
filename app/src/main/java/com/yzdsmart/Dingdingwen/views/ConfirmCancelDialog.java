package com.yzdsmart.Dingdingwen.views;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.R;

/**
 * Created by YZD on 2016/9/12.
 */
public class ConfirmCancelDialog extends Dialog {
    private Context context;
    private String contentMsg;
    private TextView contentTV;

    public ConfirmCancelDialog(Context context, String contentMsg) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.contentMsg = contentMsg;
        this.setContentView(R.layout.confirm_cancel_dialog);
    }

    @Override
    public void show() {
        super.show();
        contentTV = (TextView) findViewById(R.id.content_text);
        contentTV.setText(contentMsg);
    }
}
