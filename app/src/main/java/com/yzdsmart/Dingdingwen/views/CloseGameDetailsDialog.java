package com.yzdsmart.Dingdingwen.views;

import android.app.Dialog;
import android.content.Context;

import com.yzdsmart.Dingdingwen.R;

/**
 * Created by YZD on 2016/9/12.
 */
public class CloseGameDetailsDialog extends Dialog {

    public CloseGameDetailsDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.setContentView(R.layout.close_game_details_dialog);
    }
}
