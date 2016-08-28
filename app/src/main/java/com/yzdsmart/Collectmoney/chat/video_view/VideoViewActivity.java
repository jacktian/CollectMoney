package com.yzdsmart.Collectmoney.chat.video_view;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;

import butterknife.BindView;

/**
 * 视频显示
 * Created by YZD on 2016/8/7.
 */
public class VideoViewActivity extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {
    @Nullable
    @BindView(R.id.video_view)
    SurfaceView videoView;

    private String videoPath;
    private MediaPlayer videoPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoPath = getIntent().getStringExtra("path");

        SurfaceHolder videoHolder = videoView.getHolder();
        videoHolder.addCallback(this);
        videoPlayer = new MediaPlayer();
        videoPlayer.setOnPreparedListener(this);
        videoPlayer.setLooping(true);
        try {
            videoPlayer.setDataSource(videoPath);
        } catch (Exception e) {
            finish();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_video_view;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != videoPlayer) {
            videoPlayer.release();
        }
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param surfaceHolder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        videoPlayer.setDisplay(surfaceHolder);
        videoPlayer.prepareAsync();
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param surfaceHolder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        videoPlayer.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return false;
    }
}
