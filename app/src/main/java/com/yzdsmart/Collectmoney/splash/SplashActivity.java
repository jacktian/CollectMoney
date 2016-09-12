package com.yzdsmart.Collectmoney.splash;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;

import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.views.GetCoinDialog;

/**
 * Created by YZD on 2016/8/17.
 */
public class SplashActivity extends BaseActivity {
    private Handler splashHandler;
    private Runnable splashRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashHandler = new Handler();
        splashRunnable = new Runnable() {
            @Override
            public void run() {
                openActivity(MainActivity.class);
                closeActivity();
            }
        };
        splashHandler.postDelayed(splashRunnable, 1000);

        Dialog getCoinDialog = new GetCoinDialog(this, setNumToIcon(10));
        getCoinDialog.show();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        splashHandler.removeCallbacks(splashRunnable);
        super.onDestroy();
    }

    /**
     * 往图片添加数字
     */
    private Bitmap setNumToIcon(int num) {
        BitmapDrawable bd = (BitmapDrawable) App.getAppInstance().getResources().getDrawable(
                R.mipmap.scan_get_coin);
        Bitmap bitmap = bd.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(50);
        float margin = bitmap.getWidth() / 3;
        canvas.drawText(String.valueOf(num),
                margin,
                ((bitmap.getHeight() / 7) * 5), paint);

        return bitmap;
    }
}
