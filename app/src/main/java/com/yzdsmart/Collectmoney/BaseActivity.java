package com.yzdsmart.Collectmoney;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.utils.IntentUtils;
import com.yzdsmart.Collectmoney.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.container)
    CoordinatorLayout container;

    private Unbinder unbinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        //绑定ButterKnife
        unbinder = ButterKnife.bind(this);
        MoneyApp.getAppInstance().storeActivity(this);
    }

    //获取页面布局
    protected abstract int getLayoutResource();

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    //隐藏控件
    public static final ButterKnife.Action<View> BUTTERKNIFEGONE = new ButterKnife.Action<View>() {

        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };

    //显示控件
    public static final ButterKnife.Action<View> BUTTERKNIFEVISIBLE = new ButterKnife.Action<View>() {

        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };

    /**
     * 工具类打开activity
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, 0);
    }

    public void openActivityForResult(Class<?> pClass, int requestCode) {
        openActivity(pClass, null, requestCode);
    }

    /**
     * 工具类打开activity,并携带参数
     */
    public void openActivity(Class<?> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(this, pClass);
        if (null != pBundle) {
            intent.putExtras(pBundle);
        }
        if (0 == requestCode) {
            IntentUtils.startActivity(this, intent, 0);
        } else {
            IntentUtils.startActivity(this, intent, requestCode);
        }
    }

    /**
     * 关闭当前Activity
     */
    public void closeActivity() {
        MoneyApp.getAppInstance().removeActivity(this);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 显示snackbar
     *
     * @param msg
     */
    public void showSnackbar(String msg) {
        if (null != container) {
            Snackbar mSnackbar = Snackbar.make(container, msg, Snackbar.LENGTH_SHORT);
            View snackView = mSnackbar.getView();
            snackView.setBackgroundColor(getResources().getColor(R.color.grey));
            TextView snackTV = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
            if (null != snackTV) {
                snackTV.setTextColor(Color.WHITE);
                snackTV.setTextSize(getResources().getDimension(R.dimen.font_small));
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    snackTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                snackTV.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            mSnackbar.show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            if (Utils.isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                closeActivity();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
