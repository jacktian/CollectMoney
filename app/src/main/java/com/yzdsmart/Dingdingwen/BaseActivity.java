package com.yzdsmart.Dingdingwen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.share_sdk.OnekeyShare;
import com.yzdsmart.Dingdingwen.utils.IntentUtils;
import com.yzdsmart.Dingdingwen.utils.SnackbarUtils;
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
        getWindow().setBackgroundDrawable(null);

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
        App.getAppInstance().removeActivity(this);
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
     * 工具类打开activity,并携带参数
     */
    public void openActivityClear(Class<?> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(this, pClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            SnackbarUtils.showShortSnackbar(container, msg, Color.WHITE, getResources().getColor(R.color.colorPrimary), 1);
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

    /**
     * 显示分享九宫格
     */
    public void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("一口一个金币");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.dindinwen.com/mobile/");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("一口一个金币 http://www.dindinwen.com/mobile/");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://139.196.177.114:7288/Images/share.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.dindinwen.com/mobile/");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("一口一个金币");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("叮叮蚊");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.dindinwen.com/mobile/");

        // 启动分享GUI
        oks.show(this);
    }
}
