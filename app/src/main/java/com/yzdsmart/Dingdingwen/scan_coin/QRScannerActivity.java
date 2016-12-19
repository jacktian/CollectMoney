package com.yzdsmart.Dingdingwen.scan_coin;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.payment.PaymentActivity;
import com.yzdsmart.Dingdingwen.utils.NetworkUtils;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.ScanCoinDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.bingoogolapple.qrcode.core.QRCodeView;

/**
 * Created by YZD on 2016/8/19.
 */
public class QRScannerActivity extends BaseActivity implements QRCodeView.Delegate, QRScannerContract.QRScannerView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.qr_code_view)
    QRCodeView mQRCodeView;

    private static final String TAG = "QRScannerActivity";

    private Integer scanType;//0 扫币 1 付款

    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private static final long VIBRATE_DURATION = 200L;

    private QRScannerContract.QRScannerPresenter mPresenter;

    private Dialog getCoinDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scanType = getIntent().getExtras().getInt("scanType");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText(getResources().getString(R.string.qr_scan_title));

        new QRScannerPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        playBeep = true;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (AudioManager.RINGER_MODE_NORMAL != audioManager.getRingerMode()) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_qr_scanner;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        mQRCodeView.startSpotAndShowRect();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
        mQRCodeView.stopSpotAndHiddenRect();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        if (null != mediaPlayer) {
            mediaPlayer.release();
        }
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }

    //初始化声音
    private void initBeepSound() {
        if (playBeep && null == mediaPlayer) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (Exception e) {
                mediaPlayer = null;
            }
        }
    }

    /**
     * 扫描后声音和震动提醒
     */
    private void playBeepSoundAndVibrate() {
        if (playBeep && null != mediaPlayer) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onScanQRCodeSuccess(String result) {
        playBeepSoundAndVibrate();
        String retaCode = Uri.parse(result).getQueryParameter("RetaCode");
        if (null == retaCode || "".equals(retaCode)) {
            showSnackbar("商铺不存在");
            mQRCodeView.startSpot();
            return;
        }
        switch (scanType) {
            case 0:
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    mQRCodeView.startSpot();
                    return;
                }
                mPresenter.getCoins(Constants.GET_COIN_ACTION_CODE, retaCode, SharedPreferencesUtils.getString(QRScannerActivity.this, "cust_code", ""), SharedPreferencesUtils.getString(QRScannerActivity.this, "qLocation", ""), NetworkUtils.getIPAddress(true), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
            case 1:
                Bundle bundle = new Bundle();
                bundle.putString("bazaCode", retaCode);
                openActivity(PaymentActivity.class, bundle, 0);
                break;
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        mQRCodeView.startSpot();
    }

    @Override
    public void setPresenter(QRScannerContract.QRScannerPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetCoins(boolean flag, String msg, Double counts, String coinLogo) {
        if (!flag) {
            showSnackbar(msg);
            mQRCodeView.startSpot();
            return;
        }
        getCoinDialog = new ScanCoinDialog(this, null == coinLogo ? "" : coinLogo, counts);
        getCoinDialog.show();
        Button dialogConfirm = (Button) getCoinDialog.findViewById(R.id.dialog_confirm);
        dialogConfirm.setVisibility(View.VISIBLE);
        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != getCoinDialog) {
                    getCoinDialog.dismiss();
                    getCoinDialog = null;
                }
                closeActivity();
            }
        });
    }

    /**
     * 往图片添加数字
     */
    private Bitmap setNumToIcon(int num) {
        BitmapDrawable bd = (BitmapDrawable) App.getAppInstance().getResources().getDrawable(
                R.mipmap.scan_get_coin);
        Bitmap bitmap = bd.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        int textSize = Math.round(38 * Utils.getScreenRatio(this));

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        float margin = bitmap.getWidth() * 3 / 10;
        canvas.drawText(String.valueOf(num),
                margin,
                ((bitmap.getHeight() / 7) * 5), paint);

        return bitmap;
    }
}
