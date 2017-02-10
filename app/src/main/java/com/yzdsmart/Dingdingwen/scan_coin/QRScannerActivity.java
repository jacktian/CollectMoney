package com.yzdsmart.Dingdingwen.scan_coin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.payment.PaymentActivity;
import com.yzdsmart.Dingdingwen.register_login.login.LoginActivity;
import com.yzdsmart.Dingdingwen.time_keeper.TimeKeeperActivity;
import com.yzdsmart.Dingdingwen.utils.NetworkUtils;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.ScanCoinDialog;
import com.yzdsmart.Dingdingwen.views.SignDialog;

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

    private MediaPlayer beepMediaPlayer, failMediaPlayer, shineMediaPlayer;
    private boolean playBeep, playFail, playShine;
    private static final float BEEP_VOLUME = 0.66f;
    private boolean vibrate;
    private static final long VIBRATE_DURATION = 200L;

    private QRScannerContract.QRScannerPresenter mPresenter;

    private String retaCode = "";

    private Dialog getCoinDialog;
    private Dialog signDialog;

    private Handler timeKeeperHandler = new Handler();
    private Handler startQRHandler = new Handler();
    private Runnable timeKeeperRunnable, startQRRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            scanType = savedInstanceState.getInt("scanType");
        } else {
            scanType = getIntent().getExtras().getInt("scanType");
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        switch (scanType) {
            case 0:
                centerTitleTV.setText(getResources().getString(R.string.qr_scan_get_coin));
                break;
            case 1:
                centerTitleTV.setText(getResources().getString(R.string.qr_scan_pay));
                break;
        }

        new QRScannerPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        playBeep = true;
        playFail = true;
        playShine = true;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (AudioManager.RINGER_MODE_NORMAL != audioManager.getRingerMode()) {
            playBeep = false;
            playFail = false;
            playShine = false;
        }
        initBeepSound();
        initFailSound();
        initShineSound();
        vibrate = true;
        mQRCodeView.setDelegate(this);

        timeKeeperRunnable = new Runnable() {
            @Override
            public void run() {
                if (null != signDialog) {
                    signDialog.dismiss();
                    signDialog = null;
                }
                Bundle bundle = new Bundle();
                bundle.putString("activityCode", retaCode);
                openActivity(TimeKeeperActivity.class, bundle, 0);
                closeActivity();
            }
        };

        startQRRunnable = new Runnable() {
            @Override
            public void run() {
                mQRCodeView.startSpot();
            }
        };
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
        if (null != beepMediaPlayer) {
            if (beepMediaPlayer.isPlaying()) {
                beepMediaPlayer.stop();
            }
            beepMediaPlayer.release();
        }
        if (null != failMediaPlayer) {
            if (failMediaPlayer.isPlaying()) {
                failMediaPlayer.stop();
            }
            failMediaPlayer.release();
        }
        if (null != shineMediaPlayer) {
            if (shineMediaPlayer.isPlaying()) {
                shineMediaPlayer.stop();
            }
            shineMediaPlayer.release();
        }
        mQRCodeView.onDestroy();
        timeKeeperHandler.removeCallbacks(timeKeeperRunnable);
        startQRHandler.removeCallbacks(startQRRunnable);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            if (null == MainActivity.getInstance()) return;
            MainActivity.getInstance().chatLogin();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("scanType", scanType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (null != SharedPreferencesUtils.getString(QRScannerActivity.this, "cust_code", "") && SharedPreferencesUtils.getString(QRScannerActivity.this, "cust_code", "").length() > 0) {
                    setResult(RESULT_OK);
                    closeActivity();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                if (null != SharedPreferencesUtils.getString(QRScannerActivity.this, "cust_code", "") && SharedPreferencesUtils.getString(QRScannerActivity.this, "cust_code", "").length() > 0) {
                    setResult(RESULT_OK);
                }
                closeActivity();
                break;
        }
    }

    //初始化声音
    private void initBeepSound() {
        if (playBeep && null == beepMediaPlayer) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            beepMediaPlayer = new MediaPlayer();
            beepMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            beepMediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                beepMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                beepMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                beepMediaPlayer.prepare();
            } catch (Exception e) {
                beepMediaPlayer = null;
            }
        }
    }

    private void initFailSound() {
        if (playFail && null == failMediaPlayer) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            failMediaPlayer = new MediaPlayer();
            failMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            failMediaPlayer.setOnCompletionListener(failListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.fail);
            try {
                failMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                failMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                failMediaPlayer.prepare();
            } catch (Exception e) {
                failMediaPlayer = null;
            }
        }
    }

    private void initShineSound() {
        if (playShine && null == shineMediaPlayer) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            shineMediaPlayer = new MediaPlayer();
            shineMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            shineMediaPlayer.setOnCompletionListener(shineListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.shine);
            try {
                shineMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                shineMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                shineMediaPlayer.prepare();
            } catch (Exception e) {
                shineMediaPlayer = null;
            }
        }
    }

    /**
     * 扫描后声音和震动提醒
     */
    private void playBeepSoundAndVibrate() {
        if (playBeep && null != beepMediaPlayer) {
            beepMediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private void playFailSound() {
        if (playFail && null != failMediaPlayer) {
            failMediaPlayer.start();
        }
    }

    private void playShineSound() {
        if (playShine && null != shineMediaPlayer) {
            shineMediaPlayer.start();
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

    private final MediaPlayer.OnCompletionListener failListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private final MediaPlayer.OnCompletionListener shineListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onScanQRCodeSuccess(String result) {
        playBeepSoundAndVibrate();

        if (result.indexOf("RetaCode=") < 0 && result.indexOf("retaType=") < 0) {
            showSnackbar("二维码有误,请重新扫描");
            mQRCodeView.startSpot();
            return;
        }

        String action = "";
        if (result.indexOf("&action=") > -1) {
            action = result.split("&action=")[1];
        }
        retaCode = result.split("RetaCode=")[1].split("&retaType=")[0];
        if (null != action && (!"".equals(action))) {
            if (null == retaCode || "".equals(retaCode)) {
                showSnackbar("扫码点不存在");
                mQRCodeView.startSpot();
                return;
            }
            switch (scanType) {
                case 0:
                    if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0) {
                        openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                        return;
                    }
                    if (!Utils.isNetUsable(this)) {
                        showSnackbar(getResources().getString(R.string.net_unusable));
                        mQRCodeView.startSpot();
                        return;
                    }
                    mPresenter.scanQRCode(Constants.SIGN_ACTION_CODE, retaCode, SharedPreferencesUtils.getString(QRScannerActivity.this, "cust_code", ""), SharedPreferencesUtils.getString(QRScannerActivity.this, "qLocation", ""), NetworkUtils.getIPAddress(true), 1, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                    break;
                case 1:
                    showSnackbar("付款不支持签到");
                    mQRCodeView.startSpot();
                    break;
            }
        } else {
            if (null == retaCode || "".equals(retaCode)) {
                showSnackbar("商铺不存在");
                mQRCodeView.startSpot();
                return;
            }
            switch (scanType) {
                case 0:
                    if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0) {
                        openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                        return;
                    }
                    if (!Utils.isNetUsable(this)) {
                        showSnackbar(getResources().getString(R.string.net_unusable));
                        mQRCodeView.startSpot();
                        return;
                    }
                    mPresenter.scanQRCode(Constants.GET_COIN_ACTION_CODE, retaCode, SharedPreferencesUtils.getString(QRScannerActivity.this, "cust_code", ""), SharedPreferencesUtils.getString(QRScannerActivity.this, "qLocation", ""), NetworkUtils.getIPAddress(true), 0, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                    break;
                case 1:
                    Bundle bundle = new Bundle();
                    bundle.putString("bazaCode", retaCode);
                    openActivity(PaymentActivity.class, bundle, 0);
                    break;
            }
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
    public void onScanQRCode(boolean flag, String msg, Double counts, String coinLogo, Integer type) {
        if (!flag) {
            playFailSound();
            switch (type) {
                case 0:
                    showSnackbar(msg);
                    startQRHandler.postDelayed(startQRRunnable, 1666);
                    break;
                case 1:
                    signDialog = new SignDialog(this, coinLogo, false);
                    signDialog.setCancelable(false);
                    signDialog.show();
                    timeKeeperHandler.postDelayed(timeKeeperRunnable, 1000);
                    break;
            }
            return;
        }
        playShineSound();
        Button dialogConfirm;
        switch (type) {
            case 0:
                getCoinDialog = new ScanCoinDialog(this, null == coinLogo ? "" : coinLogo, counts);
                getCoinDialog.setCancelable(false);
                getCoinDialog.show();
                dialogConfirm = (Button) getCoinDialog.findViewById(R.id.dialog_confirm);
                dialogConfirm.setVisibility(View.VISIBLE);
                dialogConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != getCoinDialog) {
                            getCoinDialog.dismiss();
                            getCoinDialog = null;
                        }
                        mQRCodeView.startSpot();
                        showShare();
                    }
                });
                break;
            case 1:
                signDialog = new SignDialog(this, coinLogo, "本点签到成功".equals(coinLogo) ? true : false);
                signDialog.setCancelable(false);
                signDialog.show();
                timeKeeperHandler.postDelayed(timeKeeperRunnable, 1000);
                break;
        }
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
