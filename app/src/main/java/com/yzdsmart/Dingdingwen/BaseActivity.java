package com.yzdsmart.Dingdingwen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.utils.IntentUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.DynamicDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.toolBar)
    protected Toolbar toolBar;
    @Nullable
    @BindView(R.id.container)
    CoordinatorLayout container;

    private Unbinder unbinder = null;

    private DynamicDialog dynamicDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后

        //绑定ButterKnife
        unbinder = ButterKnife.bind(this);
        App.getAppInstance().storeActivity(this);

//        StatusBarUtil.statusBarLightMode(this);改变状态栏字体颜色

        if (null != toolBar) {
            setSupportActionBar(toolBar);
        }

    }

    //获取页面布局
    protected abstract int getLayoutResource();

    private final static String TAG = "Activity";

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

    //隐藏控件
    public static final ButterKnife.Action<View> BUTTERKNIFEINVISIBLE = new ButterKnife.Action<View>() {

        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.INVISIBLE);
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
        App.getAppInstance().removeActivity(this);
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
            snackView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            TextView snackTV = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
            if (null != snackTV) {
                snackTV.setTextColor(Color.WHITE);
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

    public boolean requiredVerify(EditText et) {
        String str = et.getText().toString();
        if (null != str && str.trim().length() > 0) {
            return true;
        }
        return false;
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

    /**
     * 显示进度条
     */
    public void showProgressDialog(int resourceId, String str) {
        if (null == dynamicDialog) {
            dynamicDialog = new DynamicDialog(BaseActivity.this, resourceId, str);
        }
        dynamicDialog.setCancelable(false);
        dynamicDialog.show();
    }

    /**
     * 隐藏进度条
     */
    public void hideProgressDialog() {
        if (null != dynamicDialog) {
            dynamicDialog.dismiss();
            dynamicDialog = null;
        }
    }
}
