package com.yzdsmart.Dingdingwen.qr_scan;

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
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.register_login.login.LoginActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.StaticDialog;

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

        if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0) {
            openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
            return;
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText(getResources().getString(R.string.qr_scan_title));

        new QRScannerPresenter(this, this);

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
        mQRCodeView.startSpotAndShowRect();
    }

    @Override
    public void onPause() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            MainActivity.getInstance().chatLogin();
        }
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
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            mQRCodeView.startSpot();
            return;
        }
        mPresenter.getCoins(Constants.GET_COIN_ACTION_CODE, result, Utils.getLocalIpAddress(), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
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
    public void onGetCoins(boolean flag, String msg, Integer counts) {
        if (!flag) {
            showSnackbar(msg);
            mQRCodeView.startSpot();
            return;
        }
        getCoinDialog = new StaticDialog(this, setNumToIcon(counts));
        getCoinDialog.show();
        Button dialogConfirm = (Button) getCoinDialog.findViewById(R.id.dialog_confirm);
        dialogConfirm.setVisibility(View.VISIBLE);
        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != getCoinDialog) {
                    getCoinDialog.dismiss();
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

        // 字体大小随分辨率大小变化
        // 计算与你开发时设定的屏幕大小的纵横比
        int screenWidth = Utils.deviceWidth(this);
        int screenHeight = Utils.deviceHeight(this);
        float ratioWidth = (float) screenWidth / 480;
        float ratioHeight = (float) screenHeight / 800;

        float ratio = Math.min(ratioWidth, ratioHeight);
        int textSize = Math.round(38 * ratio);

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
