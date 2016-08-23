package com.yzdsmart.Collectmoney.main.qr_scanner;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.bingoogolapple.qrcode.core.QRCodeView;

/**
 * Created by YZD on 2016/8/18.
 */
public class QRScannerFragment extends BaseFragment implements QRCodeView.Delegate {
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

    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private static final long VIBRATE_DURATION = 200L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playBeep = true;
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (AudioManager.RINGER_MODE_NORMAL != audioManager.getRingerMode()) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_qr_scanner;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.apply(hideViews, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText(getActivity().getResources().getString(R.string.qr_scan_title));

        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpotAndShowRect();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("------------------------------------QRScannerFragment is hidden " + this.isHidden());
//        if (!this.isHidden()) {
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mQRCodeView.stopSpotAndHiddenRect();
    }

    @Override
    public void onDestroyView() {
        mQRCodeView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        System.out.println("------------------------------------QRScannerFragment is hidden " + hidden);
        if (hidden) {
            mQRCodeView.stopSpotAndHiddenRect();
        } else {
            mQRCodeView.startSpotAndShowRect();
        }
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                ((MainActivity) getActivity()).backToFindMoney();
                break;
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        ((BaseActivity) getActivity()).showSnackbar(result);
        playBeepSoundAndVibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    //初始化声音
    private void initBeepSound() {
        if (playBeep && null == mediaPlayer) {
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
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
}
