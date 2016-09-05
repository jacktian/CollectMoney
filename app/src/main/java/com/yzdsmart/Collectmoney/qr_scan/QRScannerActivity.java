package com.yzdsmart.Collectmoney.qr_scan;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.utils.Utils;

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

    private static final String GET_COIN_ACTION_CODE = "88";

    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private static final long VIBRATE_DURATION = 200L;

    private QRScannerContract.QRScannerPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable closeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
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

        closeRunnable = new Runnable() {
            @Override
            public void run() {
                closeActivity();
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
        mHandler.removeCallbacks(closeRunnable);
        mQRCodeView.onDestroy();
        mediaPlayer.release();
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
        mPresenter.getCoins(GET_COIN_ACTION_CODE, result, Utils.getLocalIpAddress());
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
        showSnackbar("获得金币数：" + counts);
        mHandler.postDelayed(closeRunnable, 1500);
    }
}
